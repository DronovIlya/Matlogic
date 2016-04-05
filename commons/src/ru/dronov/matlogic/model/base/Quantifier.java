package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Quantifier extends Expression {

    private static final String TAG = Quantifier.class.getName();

    public final Variable term;
    public final Expression argument;

    public Quantifier(Variable term, Expression argument) {
        this.term = term;
        this.argument = argument;
    }

    @Override
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        boolean isBlocked = true;
        if (blocked.contains(term)) {
            isBlocked = false;
        } else {
            blocked.add(term);
        }

        Set<Variable> result = new HashSet<>();
        result.addAll(argument.getFreeVariables(blocked));

        if (isBlocked) {
            blocked.remove(term);
        }
        return result;
    }

    @Override
    public boolean compare(Expression expression, Map<Object, Object> dictionary) {
        return argument.compareInternal(((Quantifier) expression).argument, dictionary);
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        if (term.equals(from)) {
            return false;
        } else {
            return argument.replace(from, to);
        }
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        boolean isBlocked = true;
        boolean result = false;

        if (blocked.contains(term)) {
            isBlocked = false;
        } else {
            blocked.add(term);
        }

        result = argument.substitute(from, to, blocked);
        if (isBlocked) {
            blocked.remove(term);
        }
        return result;
    }

    @Override
    public String toString() {
        return getQuantifier() + term + "(" + argument + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        Quantifier quantifier = (Quantifier) obj;
        if (!term.equals(quantifier.term)) {
            return false;
        }
        return argument.equals(quantifier.argument);
    }

    @Override
    public boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary) {
        if (getClass() != expression.getClass()) {
            return false;
        }
        return argument.compareWithEquals(((Quantifier)expression).argument, variable, dictionary);
    }

    @Override
    public int hashCode() {
        int result = term != null ? term.hashCode() : 0;
        result = 31 * result + (argument != null ? argument.hashCode() : 0);
        return result;
    }

    /**
     * Used only for {@link #toString()}
     */
    protected abstract String getQuantifier();
}
