package accountservice.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Data
public class Report {
    private String id;
    private String bankId;
    private String firstName;
    private String lastName;
    private int numberOfTransactions;
    private double totalAmount;
}
