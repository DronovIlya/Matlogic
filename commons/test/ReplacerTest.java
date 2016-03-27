import junit.framework.Assert;
import org.junit.Test;
import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;
import java.util.List;

public class ReplacerTest {

    @Test
    public void testReplace1() throws IOException, ResourceNotFound {
//        List<Expression> expressions = Replacer.replaceUniversalModusPonens(new Predicate("(1)"), new Predicate("(2)"), new Predicate("(3)"),
//                new Predicate("(4)"));
//        Assert.assertEquals("((1)&(3))->((((1)&(3))->((1)&(3)))->((1)&(3)))", expressions.get(0).toString());
    }
}
