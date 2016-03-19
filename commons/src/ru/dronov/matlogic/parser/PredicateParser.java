package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.*;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PredicateParser extends Parser {

    public PredicateParser(String filename) throws FileNotFoundException {
        super(filename);
    }

    public PredicateParser(InputStream stream) {
        super(stream);
    }

    @Override
    public Expression parse() throws IOException {
        nextToken();
        return expression();
    }

    private Expression expression() throws IOException {
        Expression result = expressionOr();
        if (getToken() == Token.IMPLICATION) {
            nextToken();
            return new Implication(result, expression());
        }
        return result;
    }

    private Expression expressionOr() throws IOException {
        Expression result = expressionAnd();
        if (getToken() == Token.OR) {
            nextToken();
            return new Or(result, expressionAnd());
        }
        return result;
    }

    private Expression expressionAnd() throws IOException {
        Expression result = expressionUnary();
        if (getToken() == Token.AND) {
            nextToken();
            return new And(result, expressionUnary());
        }
        return result;
    }

    private Expression expressionUnary() throws IOException {
        switch (getToken()) {
            case NOT:
                nextToken();
                return new Negation(expressionUnary());
            case UNIVERSAL:
                nextToken();
                Term term = new Term(getDescription());
                nextToken();
                return new Universal(term, expressionUnary());
            case EXISTENCE:
                nextToken();
                term = new Term(getDescription());
                nextToken();
                return new Existance(term, expressionUnary());
            case LEFT_BRACKET:
                nextToken();
                Expression expr = expression();
                nextToken();
                return expr;
            case PREDICATE:
                return expressionPredicate();
            default:
                throw new IllegalStateException("cannot parse result");

        }
    }

    private Predicate expressionPredicate() throws IOException {
        String predicateName = getDescription();
        nextToken();
        if (getToken() == Token.LEFT_BRACKET) {
            List<Term> terms = new ArrayList<>();
            do {
                nextToken();
                terms.add(expressionTerm());
            } while (getToken() == Token.COMMA);
            nextToken();
            return new Predicate(predicateName, terms);
        } else {
            return new Variable(predicateName);
        }
    }

    private Term expressionTerm() throws IOException {
        Term result;
        if (getToken() == Token.LEFT_BRACKET) {
            nextToken();
            result = expressionTerm();
        } else {
            String termName = getDescription();
            List<Term> terms = new ArrayList<>();
            nextToken();
            if (getToken() == Token.LEFT_BRACKET) {
                do {
                    nextToken();
                    terms.add(expressionTerm());
                } while (getToken() == Token.COMMA);

                nextToken();
            }
            result = new Term(termName, terms);
        }
        return result;
    }
}
