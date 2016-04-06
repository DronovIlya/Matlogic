import ru.dronov.matlogic.base.ClassicalAxioms;
import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.exceptions.UnknownException;
import ru.dronov.matlogic.model.Implication;
import ru.dronov.matlogic.model.base.Expression;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeductionHelper {

    private final ClassicalAxioms axioms;

    private final List<Expression> proved = new ArrayList<>();
    private final List<Expression> answer = new ArrayList<>();
    private final Map<Expression, List<Expression>> modusPonens = new HashMap<>();

    private Expression alpha;
    private List<Expression> hypothesis;

    public DeductionHelper() throws IOException, ParserException {
        this.axioms = new ClassicalAxioms(ClassicalAxioms.Type.CLASSICAL);
    }

    public List<Expression> handle(List<Expression> proof, List<Expression> hypothesis) throws ResourceNotFound, UnknownException, ParserException {
        this.alpha = hypothesis.get(hypothesis.size() - 1);
        this.hypothesis = hypothesis;

        if (!hypothesis.isEmpty()) {
            for (Expression expression : proof) {

                if (handleAlpha(expression)) {
                    process(expression);
                    continue;
                }

                if (handleHypothesis(expression)) {
                    process(expression);
                    continue;
                }

                if (handleClassicalAxioms(expression)) {
                    process(expression);
                    continue;
                }

                if (handleModusPonens(expression)) {
                    process(expression);
                    continue;
                }

                throw new UnknownException(expression.toString());
            }
        }

        return answer;
    }

    private boolean handleAlpha(Expression expression) throws ResourceNotFound, ParserException {
        if (alpha.equals(expression)) {
            if (Task3Main.DEBUG) {
                System.out.println("alpha equals argument");
            }
            answer.addAll(Replacer.replaceAimplA(expression, true));
            return true;
        }
        return false;
    }

    private boolean handleHypothesis(Expression expression) throws ResourceNotFound, ParserException {
        for (Expression hypothesisEntry : hypothesis) {
            if (hypothesisEntry.equals(expression)) {
                if (Task3Main.DEBUG) {
                    System.out.println("argument contains in hypothesis = " + hypothesisEntry);
                }
                answer.addAll(Replacer.replaceAimplB(expression, alpha, true));
                return true;
            }
        }
        return false;
    }

    private boolean handleClassicalAxioms(Expression expression) throws ResourceNotFound, ParserException {
        Expression result = axioms.handle(expression);
        if (result != null) {
            if (Task3Main.DEBUG) {
                System.out.println("argument is in classical axioms, result = " + result);
            }
            answer.addAll(Replacer.replaceAimplB(expression, alpha, true));
            return true;
        }
        return false;
    }

    private boolean handleModusPonens(Expression expression) throws ResourceNotFound, ParserException {
        List<Expression> list = modusPonens.get(expression);
        if (list != null && !list.isEmpty()) {
            for (Expression entry : list) {
                if (proved.contains(entry)) {
                    if (Task3Main.DEBUG) {
                        System.out.println("argument satisfies modus ponens");
                    }
                    answer.addAll(Replacer.replaceAimplC(alpha, entry, expression, true));
                    return true;
                }
            }
        }
        return false;
    }

    private void process(Expression expression) {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (!modusPonens.containsKey(implication.right)) {
                modusPonens.put(implication.right, new ArrayList<>());
            }
            List<Expression> list = modusPonens.get(implication.right);
            if (!list.contains(implication.left)) {
                list.add(implication.left);
            }
        }
        proved.add(expression);
    }
}
