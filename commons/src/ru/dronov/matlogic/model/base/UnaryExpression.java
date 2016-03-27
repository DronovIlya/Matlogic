package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.Map;
import java.util.Set;

public abstract class UnaryExpression extends Expression {

    public final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        return expression.getFreeVariables(blocked);
    }

    @Override
    public boolean compare(Expression expression, Map<Object, Object> dictionary) {
        if (getClass() != expression.getClass()) {
            return false;
        }
        return this.expression.compare(((UnaryExpression) expression).expression, dictionary);
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        return expression.replace(from, to);
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        return expression.substitute(from, to, blocked);
    }

    @Override
    public String toString() {
        return getSign() + "(" + expression + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return expression.equals(((UnaryExpression)obj).expression);
    }

    @Override
    public int hashCode() {
        return expression != null ? expression.hashCode() : 0;
    }

    protected abstract String getSign();
}
