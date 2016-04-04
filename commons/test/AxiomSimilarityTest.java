import org.junit.Test;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;
import sun.security.util.AuthResources_zh_CN;

import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
        InputStream stream = new ByteArrayInputStream(expression.getBytes());
        Parser parser = new PredicateParser(stream);
        return parser.parse();
    }
}
