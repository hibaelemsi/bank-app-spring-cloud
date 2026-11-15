
package ma.formations.msa.accountservice.controllers;

import ma.formations.msa.accountservice.clients.CustomerRestClient;
import ma.formations.msa.accountservice.entities.BankAccount;
import ma.formations.msa.accountservice.models.Customer;
import ma.formations.msa.accountservice.repository.AccountRepository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import java.util.function.Supplier;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountRestController {

    private final AccountRepository repository;
    private final CustomerRestClient customerClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;
    public AccountRestController(
            AccountRepository repository,
            CustomerRestClient customerClient,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.repository = repository;
        this.customerClient = customerClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping
    public List<BankAccount> allAccounts() {
        return repository.findAll();
    }


    @GetMapping("/{id}")
    public BankAccount accountById(@PathVariable String id) {

        BankAccount account = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Compte introuvable"));

        return circuitBreakerFactory.create("customerService")
                .run(
                        () -> {
                            Customer customer = customerClient
                                    .findCustomerById(account.getCustomerId());
                            account.setCustomer(customer);
                            return account;
                        },
                        throwable -> {
                            account.setCustomer(null);
                            return account;
                        }
                );
    }

}
