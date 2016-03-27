import ru.dronov.matlogic.base.Axioms;
import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.*;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.Universal;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.HypothesisHolder;

import java.io.IOException;
import java.util.*;

public class PredicateHelper {

    private final HypothesisHolder holder;
    private final Axioms axioms;

    private final List<Expression> proved = new ArrayList<>();
    private final Map<Expression, List<Expression>> modusPonens = new HashMap<>();

    private final Set<Variable> hypothesisVariables = new HashSet<>();

    public int processedLines = 0;

    public PredicateHelper(HypothesisHolder holder) throws IOException {
        this.holder = holder;
        this.axioms = new Axioms();
    }

    public List<Expression> handle(List<Expression> proof) throws ResourceNotFound, RuleQuantifierException, SubstitutionException, UnknownException {
        System.out.println("handle, proof.size() = " + proof.size());
        for (Expression expression : proof) {
            processedLines++;

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

            if (handleModusPonensUniversal(expression)) {
                process(expression);
                continue;
            }

            if (handleModusPonensExistance(expression)) {
                process(expression);
                continue;
            }

            throw new UnknownException();
        }
        return proof;
    }

    private boolean handleAlpha(Expression expression) throws ResourceNotFound {
        if (holder.alpha.equals(expression)) {
            System.out.println("alpha equals expression");
            proved.addAll(Replacer.replaceAimplA(expression));
            return true;
        }
        return false;
    }

    private boolean handleHypothesis(Expression expression) throws ResourceNotFound {
        for (Expression hypothesisEntry : holder.hypothesis) {
            if (hypothesisEntry.equals(expression)) {
                System.out.println("expression contains in hypothesis = " + hypothesisEntry);
                proved.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                return true;
            }
        }
        return false;
    }

    private boolean handleClassicalAxioms(Expression expression) throws ResourceNotFound {
        Expression result = axioms.handle(expression);
        if (result != null) {
            System.out.println("expression is in classical axioms, result = " + result);
            proved.addAll(Replacer.replaceAimplB(expression, holder.alpha));
            return true;
        }
        return false;
    }

    /**
     * Axiom 11 : @x(ksi) -> (ksi[x := O]), where "O" free to substitute "x"
     */
    private boolean handleAxiom11(Expression expression) throws AxiomQuantifierException {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.left instanceof Universal) {
                System.out.println("handleAxiom11, expression satisfies axiom11");

                Term variable = ((Universal) implication.left).term;
                if (hypothesisVariables.contains(variable)) {
                    throw new AxiomQuantifierException(variable.name, expression.toString());
                }

                Expression left = ((Universal) implication.left).argument;
                Expression right = implication.right;

                if (left.equals(right)) {
                    return true;
                }



            }
        }
        return false;
    }

    private boolean handleAxiom12(Expression expression) {
        return false;
    }

    private boolean handleModusPonens(Expression expression) throws ResourceNotFound {
        List<Expression> list = modusPonens.get(expression);
        if (list != null && !list.isEmpty()) {
            for (Expression entry : list) {
                if (proved.contains(entry)) {
                    proved.addAll(Replacer.replaceAimplC(holder.alpha, entry, expression));
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

    private boolean handleModusPonensUniversal(Expression expression) throws RuleQuantifierException, ResourceNotFound, SubstitutionException {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.right instanceof Universal) {
                Expression left = implication.left;
                Expression right = ((Universal) implication.right).argument;
                Variable x = ((Universal) implication.right).term;

                Implication p = new Implication(left, right);
                if (proved.contains(p)) {
                    if (!left.replace(x, x)) {
                        if (hypothesisVariables.contains(x)) {
                            throw new RuleQuantifierException("", x.toString(), expression.toString());
                        } else {
                            proved.addAll(Replacer.replaceUniversalModusPonens(holder.alpha, right, left, x));
                            return true;
                        }
                    } else {
                        throw new SubstitutionException(x.toString(), expression.toString());
                    }
                }
            }
        }
        return false;
    }

    private boolean handleModusPonensExistance(Expression expression) throws RuleQuantifierException, SubstitutionException, ResourceNotFound {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.left instanceof Existence) {
                Expression left = implication.right;
                Expression right = ((Existence) implication.left).argument;
                Variable x = ((Existence) implication.left).term;

                Implication p = new Implication(right, left);
                if (proved.contains(p)) {
                    if (!left.replace(x, x)) {
                        if (hypothesisVariables.contains(x)) {
                            throw new RuleQuantifierException("", x.toString(), expression.toString());
                        } else {
                            proved.addAll(Replacer.replaceExistanceModusPonens(holder.alpha, right, left, x));
                            return true;
                        }
                    } else {
                        throw new SubstitutionException(x.toString(), expression.toString());
                    }
                }
            }
        }
        return false;
    }
}
