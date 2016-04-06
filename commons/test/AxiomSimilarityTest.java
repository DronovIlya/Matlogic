import org.junit.Test;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.PredicateParser;

import static org.junit.Assert.*;

import java.io.IOException;

public class AxiomSimilarityTest {

    @Test
    public void test1() throws IOException {
        Expression axiom = parseExpression("A->B->A");
        Expression expression = parseExpression("(B->C)->(D->E)->(B->C)");
        assertEquals(true, axiom.compare(expression));
    }

    @Test
    public void test2() throws IOException {
        Expression axiom = parseExpression("A->B->A");
        Expression expression = parseExpression("(B->C)->(D->E)->(B->A)");
        assertEquals(false, expression.compare(axiom));
    }

    @Test
    public void testHard() throws IOException {
        Expression axiom = parseExpression("(A->C)->((B->C)->(A|B->C))");
        Expression expression = parseExpression("((Q->O)->(E->F|I))->((B->(E->F|I))->((Q->O)|B->(E->F|I)))");
        assertEquals(true, axiom.compare(expression));
    }

    private Expression parseExpression(String expression) throws IOException {
        PredicateParser parser = new PredicateParser();
        return parser.parse(expression);
    }
}
