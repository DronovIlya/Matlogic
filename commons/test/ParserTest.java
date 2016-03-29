import org.junit.Test;
import static org.junit.Assert.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.ArithmeticParser;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;

import java.io.ByteArrayInputStream;
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
        checkResult("A->B->C", "(A)->((B)->(C))");
    }

    @Test
    public void testArithParse1() throws IOException {
        checkArithResult("a+0=a", "a+0=a");
    }

     @Test
    public void testArithParse2() throws IOException {
        checkArithResult("(0+a)'=a'->0+a'=a'", "((0+a)'=(a)')->(0+(a)'=(a)')");
    }

    private void checkResult(String expression, String expected) throws IOException {
        Expression test = parsePredicateExpression(expression);
        assertEquals(expected, test.toString());
    }

    private void checkArithResult(String expression, String expected) throws IOException {
        Expression test = parseArithExpression(expression);
        assertEquals(expected, test.toString());
    }

    private Expression parsePredicateExpression(String expression) throws IOException {
        InputStream stream = new ByteArrayInputStream(expression.getBytes());
        Parser parser = new PredicateParser(stream);
        return parser.parse();
    }

    private Expression parseArithExpression(String expression) throws IOException {
        InputStream stream = new ByteArrayInputStream(expression.getBytes());
        Parser parser = new ArithmeticParser(stream);
        return parser.parse();
    }
}
