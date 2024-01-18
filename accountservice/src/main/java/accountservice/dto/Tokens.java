package accountservice.dto;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class Tokens implements Serializable {
    private ArrayList<Token> tokens;

}
