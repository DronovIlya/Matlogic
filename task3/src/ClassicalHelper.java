import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.exceptions.UnknownException;
import ru.dronov.matlogic.exceptions.UntruthException;
import ru.dronov.matlogic.model.Negation;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;
import java.util.*;

public class ClassicalHelper {

    public int processedLines = 0;

    public ClassicalHelper() throws IOException {
    }

    public List<Expression> handle(Expression toProve) throws ResourceNotFound, UnknownException, IOException, UntruthException {
        List<Expression> result;

        List<String> variables = findAllVariables(toProve);
        result = handleRecursive(toProve, variables, 0, new HashMap<>(), new ArrayList<>());
        return result;
    }

    private List<Expression> handleRecursive(Expression expression,
                                             List<String> variables,
                                             int position,
                                             Map<String, Boolean> hypothesisValues,
                                             List<Expression> hypothesis) throws ResourceNotFound, UnknownException, IOException, UntruthException {
        if (position == variables.size()) {
            List<Expression> result = new ArrayList<>();
            boolean isTrue = expression.prove(hypothesisValues, result);
            if (!isTrue) {
                throw new UntruthException(hypothesisValues);
            }
            return result;
        }
        List<Expression> proof = new ArrayList<>();
        String currentVariable = variables.get(position);

        // add variable with value True
        hypothesis.add(new Variable(currentVariable));
        hypothesisValues.put(currentVariable, true);
        List<Expression> positive = handleRecursive(expression, variables,
                position + 1, hypothesisValues, hypothesis);
        proof.addAll(deduction(positive, hypothesis));

        // add variable with value False
        hypothesis.remove(hypothesis.size() - 1);
        hypothesis.add(new Negation(new Variable(currentVariable)));
        hypothesisValues.put(currentVariable, false);
        List<Expression> negative = handleRecursive(expression, variables,
                position + 1, hypothesisValues, hypothesis);
        proof.addAll(deduction(negative, hypothesis));

        hypothesis.remove(hypothesis.size() - 1);

        proof.addAll(Replacer.replaceANotA_B(new Variable(currentVariable),
                negative.get(negative.size() - 1)));
        return proof;
    }

    private List<Expression> deduction(List<Expression> proof, List<Expression> hypothesis) throws IOException, ResourceNotFound, UnknownException {
        DeductionHelper deduction = new DeductionHelper();
        return deduction.handle(proof, hypothesis);
    }

    private List<String> findAllVariables(Expression toProve) {
        Set<Variable> variables = toProve.getFreeVariables(new HashSet<>());
        List<String> result = new ArrayList<>();
        for (Variable variable : variables) {
            result.add(variable.toString());
        }
        Collections.sort(result);
        return result;
    }
}
