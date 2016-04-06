package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.*;

public abstract class Expression {

    /**
     * @return set of free variables in argument
     */
    public abstract Set<Variable> getFreeVariables(Set<Variable> blocked);

    /**
     * Check similarity of two expressions
     * @param expression given argument
     * @param dictionary storage contains mapping from variables of first argument to variables of second argument
     * @return true if two argument are similar, false otherwise
     */
    public abstract boolean compare(Expression expression, Map<Object, Object> dictionary);
    public boolean compare(Expression expression) {
        return compareInternal(expression, new HashMap<>());
    }

    protected boolean compareInternal(Expression expression, Map<Object, Object> dictionary) {
        if (expression instanceof Predicate) {
            return ((Predicate)expression).compare(this, dictionary);
        } else {
            if (this.getClass() != expression.getClass()) {
                return false;
            }
            return compare(expression, dictionary);
        }
    }
    /**
     * Check whether a variable "from" can be free-replaced by "to" variable
     */
    public abstract boolean replace(Variable from, Variable to);

    /**
     * Check whether substitute "from" variable to term "to" according to blocked variables(initially empty)
     * @param from theoretically free variable that should be substitute into term
     * @param to given term
     * @param blocked non-free variables
     * @return true if "from" can be substitute to term
     */
    protected abstract boolean substitute(Variable from, Term to, Set<Variable> blocked);
    public boolean substitute(Variable from, Term to) {
        return substitute(from, to, new HashSet<>());
    }

    public abstract boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary);


    /**
     * Used in Task3
     * @param values contains values for prop variables
     * @param current proof for current bundle
     * @param dictionary used for avoiding duplications
     */
    public abstract boolean prove(Map<String, Boolean> values, List<Expression> current,
                                  Set<String> dictionary) throws ResourceNotFound, ParserException;
    public boolean prove(Map<String, Boolean> values, List<Expression> current) throws ResourceNotFound, ParserException {
        return prove(values, current, new HashSet<>());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
