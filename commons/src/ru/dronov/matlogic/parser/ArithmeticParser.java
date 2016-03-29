package ru.dronov.matlogic.parser;

import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.arithmetic.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArithmeticParser extends Parser {

    public ArithmeticParser(String filename) throws FileNotFoundException {
        this(new MyInputStream(new FileInputStream(filename)));
    }

    public ArithmeticParser(InputStream in) {
        super(new MyInputStream(in));
    }

    @Override
    public HypothesisHolder parseHypothesis() throws IOException {
        HypothesisHolder result = null;
        Expression expression = parse();
        if (getToken() == Token.COMMA || getToken() == Token.DERIVABLE) {
            List<Expression> hypothesis = new ArrayList<>();
            hypothesis.add(expression);

            while (getToken() == Token.COMMA) {
                hypothesis.add(parse());
            }

            Expression beta = parse();
            Expression alpha = hypothesis.get(hypothesis.size() - 1);
            hypothesis.remove(hypothesis.size() - 1);
            result = new HypothesisHolder(hypothesis, alpha, beta);
        }
        return result;
    }

    @Nullable
    @Override
    public Expression parse() throws IOException {
        nextToken();
        while (getToken() == Token.NEW_LINE) {
            nextToken();
        }
        if (getToken() == Token.END_FILE) {
            return null;
        }
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
                Variable term = new Variable(getDescription());
                nextToken();
                return new Universal(term, expressionUnary());
            case EXISTENCE:
                nextToken();
                term = new Variable(getDescription());
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
                String name = getDescription();
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
