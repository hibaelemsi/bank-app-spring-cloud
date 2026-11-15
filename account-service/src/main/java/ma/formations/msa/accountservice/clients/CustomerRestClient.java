
package ma.formations.msa.accountservice.clients;

import ma.formations.msa.accountservice.models.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "customer-service")
public interface CustomerRestClient {

    @GetMapping("/customers")
    List<Customer> allCustomers();

    @GetMapping("/customers/{id}")
    Customer findCustomerById(@PathVariable("id") Long id);
}
