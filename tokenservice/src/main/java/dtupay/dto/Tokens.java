package dtupay.dto;

import lombok.*;

import java.io.Serializable;
import java.util.Stack;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class Tokens implements Serializable {
    private Stack<Token> stack = new Stack<>();

    public void addToken(Token token) {
        if (stack.size() < 6) {
            stack.push(token);
        } else {
            System.out.println("The stack is full, cannot add more tokens.");
        }
    }

    public boolean isEmpty() {
        return stack.empty();
    }

    public Token spendToken() {
        if (!stack.empty()) {
            return stack.pop();
        } else {
            System.out.println("The stack is empty, cannot spend a token.");
            return null;
        }
    }

}