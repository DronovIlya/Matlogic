package ru.dronov.matlogic;

public class Or extends BinaryExpression {

    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getSign() {
        return Token.OR.getValue();
    }
}
