import com.senfo.parser.Lexer;
import com.senfo.parser.Token;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String input = "2 + 2";
        final List<Token> tokens = new Lexer(input).tokenize();

        for (Token token : tokens) {
            System.out.println(token.getType());
        }
    }
}