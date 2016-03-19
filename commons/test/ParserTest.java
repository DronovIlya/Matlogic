import org.junit.Test;
import static org.junit.Assert.*;
import ru.dronov.matlogic.Expression;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ParserTest {

    @Test
    public void testParse1() throws IOException {
        checkResult("@b?bP(f(a),g(b))",
                    "@b(?b(P(f(a),g(b))))");
    }

    @Test
    public void testParse2() throws IOException {
        checkResult("@b1?b1(Q(g(a),f(b))->P(f(a),g(b1)))",
                    "@b1(?b1((Q(g(a),f(b)))->(P(f(a),g(b1)))))");
    }

    @Test
    public void testParse3() throws IOException {
        checkResult("A->b->C", "A->(b->C)");
    }

    private void checkResult(String expression, String expected) throws IOException {
        Expression test = parseExpression(expression);
        assertEquals(expected, test.toString());
    }

    private Expression parseExpression(String expression) throws IOException {
        InputStream stream = new ByteArrayInputStream(expression.getBytes());
        Parser parser = new PredicateParser(stream);
        return parser.parse();
    }
}
