package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;

public abstract class Parser {

    protected LexemeParser lexemParser;

    protected Parser() {
    }

    protected Parser(String input) {
        lexemParser = new LexemeParser(input);
    }

    public Expression parse(String input) throws IOException, ParserException {
        lexemParser = new LexemeParser(input);
        return parse();
    }

    public void next() {
        lexemParser.nextToken();
    }

    public Lexeme getLexeme() {
        return lexemParser.type;
    }

    public String getLexemeName() {
        return lexemParser.lexemeName;
    }


    public Expression parse() throws IOException, ParserException {
        next();
        if (getLexeme() == Lexeme.END_LINE) {
            return null;
        }
        return expression();
    }

    protected Expression expression() throws IOException, ParserException {
        Expression result = expressionOr();
        if (getLexeme() == Lexeme.IMPLICATION) {
            next();
            return new Implication(result, expression());
        }
        return result;
    }

    protected Expression expressionOr() throws IOException, ParserException {
        Expression result = expressionAnd();
        while (getLexeme() == Lexeme.OR) {
            next();
            result = new Or(result, expressionAnd());
        }
        return result;
    }

    protected Expression expressionAnd() throws IOException, ParserException {
        Expression result = expressionUnary();
        while (getLexeme() == Lexeme.AND) {
            next();
            result = new And(result, expressionUnary());
        }
        return result;
    }

    protected Expression expressionUnary() throws IOException, ParserException {
        switch (getLexeme()) {
            case NOT:
                next();
                return new Negation(expressionUnary());
            case UNIVERSAL:
            case EXISTENCE:
                Lexeme lexeme = getLexeme();
                next();
                Variable term = new Variable(getLexemeName());
                next();

                if (lexeme == Lexeme.UNIVERSAL) {
                    return new Universal(term, expressionUnary());
                } else {
                    return new Existence(term, expressionUnary());
                }
            case LEFT_BRACKET:
                next();
                Expression expr = expression();
                next();
                return expr;
            default:
                return expressionPredicate();
        }
    }

    protected abstract Expression expressionPredicate() throws IOException, ParserException;

}
