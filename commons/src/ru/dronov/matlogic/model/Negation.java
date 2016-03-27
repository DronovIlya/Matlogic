package ru.dronov.matlogic.model;

import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.base.UnaryExpression;
import ru.dronov.matlogic.parser.Token;

public class Negation extends UnaryExpression {

    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    protected String getSign() {
        return Token.NOT.getValue();
    }
}
