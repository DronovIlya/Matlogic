package ru.dronov.matlogic;

public abstract class UnaryExpression extends Expression {

    public final Expression expression;

    public UnaryExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return getSign() + "(" + expression + ")";
    }

    protected abstract String getSign();
}
