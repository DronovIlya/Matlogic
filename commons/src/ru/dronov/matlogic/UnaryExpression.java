package ru.dronov.matlogic;

import java.util.Set;

public abstract class UnaryExpression extends Expression {

    public final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Set<String> getFreeVariables() {
        return expression.getFreeVariables();
    }

    @Override
    public String toString() {
        return getSign() + "(" + expression + ")";
    }

    protected abstract String getSign();
}
