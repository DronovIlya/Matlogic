package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.Map;
import java.util.Set;

public abstract class UnaryExpression extends Expression {

    public final Expression argument;

    public UnaryExpression(Expression expression) {
        this.argument = expression;
    }

    @Override
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        return argument.getFreeVariables(blocked);
    }

    @Override
    public boolean compare(Expression expression, Map<Object, Object> dictionary) {
        return this.argument.compareInternal(((UnaryExpression) expression).argument, dictionary);
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        return argument.replace(from, to);
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        return argument.substitute(from, to, blocked);
    }

    @Override
    public String toString() {
        return getSign() + "(" + argument + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return argument.equals(((UnaryExpression)obj).argument);
    }

    @Override
    public boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary) {
        if (getClass() != expression.getClass()) {
            return false;
        }
        return argument.compareWithEquals(expression, variable, dictionary);
    }

    @Override
    public int hashCode() {
        return argument != null ? argument.hashCode() : 0;
    }

    protected abstract String getSign();
}
