import com.senfo.lib.Variables;
import com.senfo.parser.Lexer;
import com.senfo.parser.Parser;
import com.senfo.parser.Token;
import com.senfo.parser.ast.IExpression;
import com.senfo.parser.ast.IStatement;
import com.senfo.parser.ast.VariableExpression;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        final String input = new String(Files.readAllBytes(Paths.get("example/program.txt")), "UTF-8");
        final List<Token> tokens = new Lexer(input).tokenize();
        System.out.println(tokens);

        final List<IStatement> expressions = new Parser(tokens).parse();
        for (IStatement statement : expressions) {
            System.out.println(statement);
        }

        for (IStatement statement : expressions) {
            statement.execute();
        }
    }
}