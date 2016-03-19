package ru.dronov.matlogic;

public class Negation extends UnaryExpression {

    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    protected String getSign() {
        return Token.NOT.getValue();
    }
}
