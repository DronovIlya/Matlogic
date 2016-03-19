package ru.dronov.matlogic;

public class And extends BinaryExpression {

    public And(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return Token.AND.getValue();
    }

}
