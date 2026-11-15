
package ma.formations.msa.accountservice;

import ma.formations.msa.accountservice.clients.CustomerRestClient;
import ma.formations.msa.accountservice.entities.AccountType;
import ma.formations.msa.accountservice.entities.BankAccount;
import ma.formations.msa.accountservice.models.Customer;
import ma.formations.msa.accountservice.repository.AccountRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableFeignClients
public class AccountServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner initAccounts(
            AccountRepository repository,
            CustomerRestClient customerClient) {

        return args -> {
            List<Customer> customers = customerClient.allCustomers();

            for (Customer customer : customers) {

                repository.save(BankAccount.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(new Date())
                        .balance(new BigDecimal("15000"))
                        .currency("MAD")
                        .type(AccountType.CURRENT_ACCOUNT)
                        .customerId(customer.getId())
                        .build());

                repository.save(BankAccount.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(new Date())
                        .balance(new BigDecimal("30000"))
                        .currency("MAD")
                        .type(AccountType.SAVING_ACCOUNT)
                        .customerId(customer.getId())
                        .build());
            }
        };
    }
}
