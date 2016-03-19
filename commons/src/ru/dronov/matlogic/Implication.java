package ru.dronov.matlogic;

public class Implication extends BinaryExpression {

    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return Token.IMPLICATION.getValue();
    }
}
