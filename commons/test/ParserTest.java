import org.junit.Test;
import static org.junit.Assert.*;

import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.*;
import ru.dronov.matlogic.parser.arithmetic.ArithmeticParser;
import ru.dronov.matlogic.parser.predicate.PredicateParser;

import java.io.IOException;

public class ParserTest {

    @Test
    public void testParse1() throws IOException, ParserException {
        checkResult("@b?bP(f(a),g(b))",
                    "@b(?b(P(f(a),g(b))))");
    }

    @Test
    public void testParse2() throws IOException, ParserException {
        checkResult("@b1?b1(Q(g(a),f(b))->P(f(a),g(b1)))",
                    "@b1(?b1((Q(g(a),f(b)))->(P(f(a),g(b1)))))");
    }

    @Test
    public void testParse3() throws IOException, ParserException {
        checkResult("A->B->C", "(A)->((B)->(C))");
    }

    @Test
    public void testArithParse1() throws IOException, ParserException {
        checkArithResult("a+0=a", "a+0=a");
    }

     @Test
    public void testArithParse2() throws IOException, ParserException {
        checkArithResult("(0+a)'=a'->0+a'=a'", "((0+a)'=(a)')->(0+(a)'=(a)')");
    }

    private void checkResult(String expression, String expected) throws IOException, ParserException {
        Expression test = parsePredicateExpression(expression);
        assertEquals(expected, test.toString());
    }

    private void checkArithResult(String expression, String expected) throws IOException, ParserException {
        Expression test = parseArithExpression(expression);
        assertEquals(expected, test.toString());
    }

    private Expression parsePredicateExpression(String expression) throws IOException, ParserException {
        Parser parser = new PredicateParser();
        return parser.parse();
    }

    private Expression parseArithExpression(String expression) throws IOException, ParserException {
        Parser parser = new ArithmeticParser();
        return parser.parse(expression);
    }
}
