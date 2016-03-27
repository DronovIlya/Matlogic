package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Expression {

    /**
     * @return set of free variables in expression
     */
    public abstract Set<Variable> getFreeVariables(Set<Variable> blocked);

    /**
     * Check similarity of two expressions
     * @param expression given expression
     * @param dictionary storage contains mapping from variables of first expression to variables of second expression
     * @return true if two expression are similar, false otherwise
     */
    public abstract boolean compare(Expression expression, Map<Object, Object> dictionary);
    public boolean compare(Expression expression) {
        return compare(expression, new HashMap<>());
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

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
