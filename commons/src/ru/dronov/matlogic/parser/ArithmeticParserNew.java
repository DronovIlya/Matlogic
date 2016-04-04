package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.model.Existence;
import ru.dronov.matlogic.model.Negation;
import ru.dronov.matlogic.model.Universal;
import ru.dronov.matlogic.model.arithmetic.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArithmeticParserNew extends ParserNew {

    public ArithmeticParserNew() {
    }

    @Override
    protected Expression expressionUnary() throws IOException {
        switch (getToken()) {
            case NOT:
                nextToken();
                return new Negation(expressionUnary());
            case UNIVERSAL:
                nextToken();
                Variable term = new Variable(getTokenName());
                nextToken();
                return new Universal(term, expressionUnary());
            case EXISTENCE:
                nextToken();
                term = new Variable(getTokenName());
                nextToken();
                return new Existence(term, expressionUnary());
            case LEFT_BRACKET:
                nextToken();
                Expression expr = expression();
                nextToken();
                return expr;
            default:
                return expressionPredicate();
        }
    }

    private Predicate expressionPredicate() throws IOException {
        if (getToken() == Token.PREDICATE) {
            String predicateName = getTokenName();
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
                return new Predicate(predicateName);
            }
        } else {
            Term left = expressionTerm();
            nextToken();
            Term right = expressionTerm();
            return new Equals(left, right);
        }
    }

    private Term expressionTerm() throws IOException {
        Term term = expressionSum();
        while (getToken() == Token.PLUS) {
            nextToken();
            term = new Plus(Arrays.asList(term, expressionSum()));
        }
        return term;
    }

    private Term expressionSum() throws IOException {
        Term term = expressionMul();
        while (getToken() == Token.MUL) {
            nextToken();
            term = new Mul(Arrays.asList(term, expressionMul()));
        }
        return term;
    }

    private Term expressionMul() throws IOException {
        Term result;
        switch (getToken()) {
            case ZERO:
                nextToken();
                result = new Zero();
                break;
            case LEFT_BRACKET:
            case LEFT_BRACKET_SQUARE:
                nextToken();
                result = expressionTerm();
                nextToken();
                break;
            default:
                String name = getTokenName();
                nextToken();
                if (getToken() == Token.LEFT_BRACKET) {
                    List<Term> terms = new ArrayList<>();
                    do {
                        nextToken();
                        terms.add(expressionTerm());
                    } while (getToken() == Token.COMMA);
                    nextToken();
                    result = new Term(name, terms);
                } else {
                    result = new Variable(name);
                }
                break;
        }

        while (getToken() == Token.STROKE) {
            result = new Stroke(Collections.singletonList(result));
            nextToken();
        }
        return result;
    }
    
}
