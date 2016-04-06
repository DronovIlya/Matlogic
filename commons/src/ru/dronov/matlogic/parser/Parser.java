package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.utils.Texts;

import java.io.IOException;

public abstract class Parser {

    protected TokenParser tokenParser;

    protected Parser() {
    }

    protected Parser(String input) {
        tokenParser = new TokenParser(input);
    }

    public Expression parse(String input) throws IOException {
        tokenParser = new TokenParser(input);
        return parse();
    }

    public void nextToken() {
        tokenParser.nextToken();
    }

    public Token getToken() {
        return tokenParser.currentTokenType;
    }

    public String getTokenName() {
        return tokenParser.currentTokenName;
    }


    public Expression parse() throws IOException {
        nextToken();
        if (getToken() == Token.END_LINE) {
            return null;
        }
        return expression();
    }

    protected Expression expression() throws IOException {
        Expression result = expressionOr();
        if (getToken() == Token.IMPLICATION) {
            nextToken();
            return new Implication(result, expression());
        }
        return result;
    }

    protected Expression expressionOr() throws IOException {
        Expression result = expressionAnd();
        if (getToken() == Token.OR) {
            nextToken();
            return new Or(result, expressionAnd());
        }
        return result;
    }

    protected Expression expressionAnd() throws IOException {
        Expression result = expressionUnary();
        if (getToken() == Token.AND) {
            nextToken();
            return new And(result, expressionUnary());
        }
        return result;
    }

    protected abstract Expression expressionUnary() throws IOException;
}
