package ru.dronov.matlogic.model;

import ru.dronov.matlogic.model.base.BinaryExpression;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.Token;

public class Or extends BinaryExpression {

    public Or(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return Token.OR.getValue();
    }
}
