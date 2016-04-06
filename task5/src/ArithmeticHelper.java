import ru.dronov.matlogic.base.ArithmeticAxioms;
import ru.dronov.matlogic.base.ClassicalAxioms;
import ru.dronov.matlogic.exceptions.*;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.arithmetic.Stroke;
import ru.dronov.matlogic.model.arithmetic.Zero;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.Universal;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;
import java.util.*;

public class ArithmeticHelper {

    private final ClassicalAxioms classicalAxioms;
    private final ArithmeticAxioms arithmeticAxioms;

    /**
     * Used for checking classical modus ponens
     */
    private final List<Expression> proved = new ArrayList<>();
    private final Map<Expression, List<Expression>> modusPonens = new HashMap<>();

    public int processedLines = 0;

    public ArithmeticHelper() throws IOException, ParserException {
        this.classicalAxioms = new ClassicalAxioms();
        this.arithmeticAxioms = new ArithmeticAxioms();
    }

    public void handle(List<Expression> proof) throws ResourceNotFound, RuleQuantifierException, SubstitutionException, UnknownException, AxiomQuantifierException, TermSubstituteException {
        if (Task5Main.DEBUG) {
            System.out.println("handle, proof.size() = " + proof.size());
        }
        for (Expression expression : proof) {
            processedLines++;

            if (handleClassicalAxioms(expression)) {
                process(expression);
                continue;
            }

            if (handleArithmeticAxioms(expression)) {
                process(expression);
                continue;
            }

            if (handleModusPonens(expression)) {
                process(expression);
                continue;
            }

            if (handleArithAxiom9(expression)) {
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
    }

    private boolean handleClassicalAxioms(Expression expression) throws ResourceNotFound {
        Expression result = classicalAxioms.handle(expression);
        if (result != null) {
            if (Task5Main.DEBUG) {
                System.out.println("argument is in classical classicalAxioms, result = " + result);
            }
            return true;
        }
        return false;
    }

    private boolean handleArithmeticAxioms(Expression expression) throws ResourceNotFound {
        Expression result = arithmeticAxioms.handle(expression);
        if (result != null) {
            if (Task5Main.DEBUG) {
                System.out.println("argument is in classical classicalAxioms, result = " + result);
            }
            return true;
        }
        return false;
    }

    /**
     * Axiom 11 : @x(ksi) -> (ksi[x := O]), where "O" free to substitute "x"
     */
    private boolean handleAxiom11(Expression expression) throws AxiomQuantifierException, TermSubstituteException, ResourceNotFound {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.left instanceof Universal) {

                Variable variable = ((Universal) implication.left).term;
                Expression left = ((Universal) implication.left).argument;
                Expression right = implication.right;

                if (left.equals(right)) {
                    if (Task5Main.DEBUG) {
                        System.out.println("argument satisfies axiom11");
                    }
                    return true;
                }

                Map<Object, Object> map = new HashMap<>();
                boolean compare = left.compareWithEquals(right, variable, map);
                if (compare) {
                    Term result = (Term) map.get(variable.name);
                    if (result != null) {
                        if (left.substitute(variable, result)) {
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
    private boolean handleAxiom12(Expression expression) throws AxiomQuantifierException, ResourceNotFound, TermSubstituteException {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.right instanceof Existence) {
                if (Task5Main.DEBUG) {
                    System.out.println("argument satisfies axiom12");
                }

                Variable variable = ((Existence) implication.right).term;
                Expression left = ((Existence) implication.right).argument;
                Expression right = implication.left;

                if (left.equals(right)) {
                    if (Task5Main.DEBUG) {
                        System.out.println("argument satisfies axiom12");
                    }
                    return true;
                }

                Map<Object, Object> map = new HashMap<>();
                boolean compare = left.compareWithEquals(right, variable, map);
                if (compare) {
                    Term result = (Term) map.get(variable.name);
                    if (result != null) {
                        if (left.substitute(variable, result)) {
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

    private boolean handleModusPonens(Expression expression) throws ResourceNotFound {
        List<Expression> list = modusPonens.get(expression);
        if (list != null && !list.isEmpty()) {
            for (Expression entry : list) {
                if (proved.contains(entry)) {
                    if (Task5Main.DEBUG) {
                        System.out.println("argument satisfies modus ponens");
                    }
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
                        return true;
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

    private boolean handleModusPonensExistence(Expression expression) throws RuleQuantifierException, SubstitutionException, ResourceNotFound {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            if (implication.left instanceof Existence) {
                Expression left = implication.right;
                Expression right = ((Existence) implication.left).argument;
                Variable x = ((Existence) implication.left).term;

                Implication p = new Implication(right, left);
                if (proved.contains(p)) {
                    if (!left.replace(x, x)) {
                        return true;
                    } else {
                        throw new SubstitutionException(x.toString(), expression.toString());
                    }
                }
            }
        }
        return false;
    }

    private boolean handleArithAxiom9(Expression expression) {
        if (expression instanceof Implication) {
            Implication implication = (Implication) expression;
            Expression right = implication.right;
            Expression left = implication.left;
            if (left instanceof And) {
                And and = (And) left;
                if (and.right instanceof Universal) {
                    Universal universal = (Universal) and.right;
                    Variable variable = universal.term;

                    Map<Object, Object> map = new HashMap<>();
                    boolean compare = right.compareWithEquals(and.left, variable, map);
                    if (compare) {
                        Term result = (Term) map.get(variable.name);
                        Zero zero = new Zero();
                        if (result != null && result.equals(zero)) {
                            Expression base = universal.argument;
                            if (base instanceof Implication) {
                                Implication baseImpl = (Implication) base;

                                map.clear();
                                compare = right.compareWithEquals(baseImpl.right, variable, map);
                                if (compare) {
                                    result = (Term) map.get(variable.name);

                                    Stroke stroke = new Stroke(Collections.singletonList(variable));
                                    if (result != null && result.equals(stroke) && baseImpl.left.equals(right)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
