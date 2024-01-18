package accountservice.dto;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class Account implements Serializable {
    private String accountId;
    private String bankId;
    private String firstName;
    private String lastName;
    private String identifier;
    private AccountType accountType;
}
