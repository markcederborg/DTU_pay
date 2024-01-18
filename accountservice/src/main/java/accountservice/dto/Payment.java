package accountservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@ToString
public class Payment {
    private String id;
    private String merchantId;
    private Token token;
    private int amount;
}
