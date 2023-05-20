import com.senfo.lib.Variables;
import com.senfo.parser.Lexer;
import com.senfo.parser.Parser;
import com.senfo.parser.Token;
import com.senfo.parser.ast.IExpression;
import com.senfo.parser.ast.IStatement;
import com.senfo.parser.ast.VariableExpression;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String input = "word = 2 + 2\nword2 = PI + word";
        final List<Token> tokens = new Lexer(input).tokenize();
        System.out.println(tokens);

        final List<IStatement> expressions = new Parser(tokens).parse();
        for (IStatement statement : expressions) {
            System.out.println(statement);
        }

        for (IStatement statement : expressions) {
            statement.execute();
        }

        System.out.printf("%s = %f\n", "word", Variables.get("word"));
        System.out.printf("%s = %f", "word2", Variables.get("word2"));
    }
}