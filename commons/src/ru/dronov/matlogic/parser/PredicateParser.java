package ru.dronov.matlogic.parser;

import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.model.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.*;

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
            return new Predicate(predicateName);
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
