import ru.dronov.matlogic.base.ClassicalAxioms;
import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.*;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.Universal;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.predicate.HypothesisHolder;

import java.io.IOException;
import java.util.*;

public class PredicateHelper {

    private final HypothesisHolder holder;
    private final ClassicalAxioms axioms;

    /**
     * Used for checking classical modus ponens
     */
    private final List<Expression> proved = new ArrayList<>();
    private final List<Expression> answer = new ArrayList<>();
    private final Map<Expression, List<Expression>> modusPonens = new HashMap<>();

    private final Set<Variable> hypothesisVariables;

    public int processedLines = 0;

    public PredicateHelper(HypothesisHolder holder) throws IOException, ParserException {
        this.holder = holder;
        this.axioms = new ClassicalAxioms();
        this.hypothesisVariables = holder.alpha.getFreeVariables(new HashSet<>());
    }

    public List<Expression> handle(List<Expression> proof) throws ResourceNotFound, RuleQuantifierException,
            SubstitutionException, UnknownException, AxiomQuantifierException, TermSubstituteException, ParserException {
        if (Task4Main.DEBUG) {
            System.out.println("handle, proof.size() = " + proof.size());
        }
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

            if (handleAxiom11(expression) || handleAxiom12(expression)) {
                process(expression);
                continue;
            }

            if (handleModusPonensUniversal(expression)) {
                process(expression);
                continue;
            }

            if (handleModusPonensExistence(expression)) {
                process(expression);
                continue;
            }

            throw new UnknownException(expression.toString());
        }
        return answer;
    }

    private boolean handleAlpha(Expression expression) throws ResourceNotFound, ParserException {
        if (holder.alpha.equals(expression)) {
            if (Task4Main.DEBUG) {
                System.out.println("alpha equals argument");
            }
            answer.addAll(Replacer.replaceAimplAPredicate(expression));
            return true;
        }
        return false;
    }

    private boolean handleHypothesis(Expression expression) throws ResourceNotFound, ParserException {
        for (Expression hypothesisEntry : holder.hypothesis) {
            if (hypothesisEntry.equals(expression)) {
                if (Task4Main.DEBUG) {
                    System.out.println("argument contains in hypothesis = " + hypothesisEntry);
                }
                answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                return true;
            }
        }
        return false;
    }

    private boolean handleClassicalAxioms(Expression expression) throws ResourceNotFound, ParserException {
        Expression result = axioms.handle(expression);
        if (result != null) {
            if (Task4Main.DEBUG) {
                System.out.println("argument is in classical axioms, result = " + result);
            }
            answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
            return true;
        }
        return false;
    }

    /**
     * Axiom 11 : @x(ksi) -> (ksi[x := O]), where "O" free to substitute "x"
     */
    private boolean handleAxiom11(Expression expression) throws AxiomQuantifierException, TermSubstituteException, ResourceNotFound, ParserException {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.left instanceof Universal) {

                Variable variable = ((Universal) implication.left).term;
                if (hypothesisVariables.contains(variable)) {
                    throw new AxiomQuantifierException(variable.name, expression.toString());
                }

                Expression left = ((Universal) implication.left).argument;
                Expression right = implication.right;

                if (left.equals(right)) {
                    if (Task4Main.DEBUG) {
                        System.out.println("argument satisfies axiom11");
                    }
                    answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                    return true;
                }

                Map<Object, Object> map = new HashMap<>();
                boolean compare = left.compareWithEquals(right, variable, map);
                if (compare) {
                    Term result = (Term) map.get(variable.name);
                    if (result != null) {
                        if (left.substitute(variable, result)) {
                            if (Task4Main.DEBUG) {
                                System.out.println("argument satisfies axiom11");
                            }
                            answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                            return true;
                        } else {
                            throw new TermSubstituteException(result.toString(), expression.toString(), variable.toString());
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Axiom 12 : (ksi[x := O]) -> ?x(ksi), where "O" free to substitute "x"
     */
    private boolean handleAxiom12(Expression expression) throws AxiomQuantifierException, ResourceNotFound, TermSubstituteException, ParserException {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.right instanceof Existence) {
                if (Task4Main.DEBUG) {
                    System.out.println("argument satisfies axiom12");
                }

                Variable variable = ((Existence) implication.right).term;
                if (hypothesisVariables.contains(variable)) {
                    throw new AxiomQuantifierException(variable.name, expression.toString());
                }

                Expression left = ((Existence) implication.right).argument;
                Expression right = implication.left;

                if (left.equals(right)) {
                    if (Task4Main.DEBUG) {
                        System.out.println("argument satisfies axiom12");
                    }
                    answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                    return true;
                }

                Map<Object, Object> map = new HashMap<>();
                boolean compare = left.compareWithEquals(right, variable, map);
                if (compare) {
                    Term result = (Term) map.get(variable.name);
                    if (result != null) {
                        if (left.substitute(variable, result)) {
                            if (Task4Main.DEBUG) {
                                System.out.println("argument satisfies axiom12");
                            }
                            answer.addAll(Replacer.replaceAimplB(expression, holder.alpha));
                            return true;
                        } else {
                            throw new TermSubstituteException(result.toString(), expression.toString(), variable.toString());
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean handleModusPonens(Expression expression) throws ResourceNotFound, ParserException {
        List<Expression> list = modusPonens.get(expression);
        if (list != null && !list.isEmpty()) {
            for (Expression entry : list) {
                if (proved.contains(entry)) {
                    if (Task4Main.DEBUG) {
                        System.out.println("argument satisfies modus ponens");
                    }
                    answer.addAll(Replacer.replaceAimplC(holder.alpha, entry, expression));
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

    /**
     * Additional modus ponens condition for predicate :
     * (phi) -> (ksi)
     * (phi) -> @x(ksi), where "x" not free-substitute to phi
     */
    private boolean handleModusPonensUniversal(Expression expression) throws RuleQuantifierException, ResourceNotFound, SubstitutionException, ParserException {
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
                            if (Task4Main.DEBUG) {
                                System.out.println("argument satisfies modus ponens for Universal");
                            }
                            answer.addAll(Replacer.replaceUniversalModusPonens(holder.alpha, right, left, x));
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

    /**
     * Additional modus ponens condition for existence :
     * (ksi) -> (phi)
     * ?x(ksi) -> (phi), where "x" not free-substitute to phi
     */

    private boolean handleModusPonensExistence(Expression expression) throws RuleQuantifierException, SubstitutionException, ResourceNotFound, ParserException {
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
                            if (Task4Main.DEBUG) {
                                System.out.println("argument satisfies modus ponens for Existence");
                            }
                            answer.addAll(Replacer.replaceExistanceModusPonens(holder.alpha, right, left, x));
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
