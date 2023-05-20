import com.senfo.parser.Lexer;
import com.senfo.parser.Parser;
import com.senfo.parser.Token;
import com.senfo.parser.ast.IExpressionNode;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String input = "(2.86 + 2) + #2d";
        final List<Token> tokens = new Lexer(input).tokenize();

        final List<IExpressionNode> expressions = new Parser(tokens).parse();

        for (IExpressionNode expression : expressions) {
            System.out.println(expression + " = " + expression.eval());
        }
    }
}