package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.model.Negation;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;

public class ClassicalParser extends Parser {

    public ClassicalParser() {
        super();
    }

    public ClassicalParser(String input) {
        super(input);
    }

    @Override
    protected Expression expressionUnary() throws IOException {
        switch (getToken()) {
            case NOT:
                nextToken();
                return new Negation(expressionUnary());
            case LEFT_BRACKET:
                nextToken();
                Expression expression = expression();
                nextToken();
                return expression;
            case PREDICATE:
            case TERM:
                String name = getTokenName();
                nextToken();
                return new Variable(name);
            default:
                throw new IllegalStateException("error while parsing data");
        }
    }
}
