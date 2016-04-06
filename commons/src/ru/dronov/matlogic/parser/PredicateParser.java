package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.model.Existence;
import ru.dronov.matlogic.model.Negation;
import ru.dronov.matlogic.model.Universal;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredicateParser extends Parser {

    public PredicateParser(String hypothesis) {
        super(hypothesis);
    }

    public PredicateParser() {
    }

    public HypothesisHolder parseHypothesis() throws IOException {
        HypothesisHolder holder = null;
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
            holder = new HypothesisHolder(hypothesis, alpha, beta);
        }
        return holder;
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
            case PREDICATE:
                return expressionPredicate();
            default:
                throw new IllegalStateException("cannot parse result");

        }
    }

    private Predicate expressionPredicate() throws IOException {
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
    }

    private Term expressionTerm() throws IOException {
        Term result;
        if (getToken() == Token.LEFT_BRACKET) {
            nextToken();
            result = expressionTerm();
        } else {
            String name = getTokenName();
            List<Term> terms = new ArrayList<>();
            nextToken();
            if (getToken() == Token.LEFT_BRACKET) {
                do {
                    nextToken();
                    terms.add(expressionTerm());
                } while (getToken() == Token.COMMA);

                nextToken();
                result = new Term(name, terms);
            } else {
                result = new Variable(name);
            }
        }
        return result;
    }
}
