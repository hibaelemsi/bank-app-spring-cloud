
package ma.formations.msa.accountservice.entities;

import jakarta.persistence.*;
import lombok.*;
import ma.formations.msa.accountservice.models.Customer;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    @Id
    private String id;

    private Date createdAt;

    private BigDecimal balance;

    private String currency;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private Long customerId;

    @Transient
    private Customer customer;
}
