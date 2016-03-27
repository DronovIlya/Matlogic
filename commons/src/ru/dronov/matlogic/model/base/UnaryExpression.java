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
    public boolean compare(Expression expression, Map<String, Expression> dictionary) {
        if (getClass() != expression.getClass()) {
            return false;
        }
        return expression.compare(((UnaryExpression) expression).expression, dictionary);
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

    protected abstract String getSign();
}
