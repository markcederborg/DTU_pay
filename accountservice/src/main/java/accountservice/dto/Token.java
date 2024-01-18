package accountservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class Token {
    @Builder.Default private final String value = UUID.randomUUID().toString();
}
