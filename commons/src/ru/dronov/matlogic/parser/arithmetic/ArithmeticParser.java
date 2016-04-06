package ru.dronov.matlogic.parser.arithmetic;

import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.model.arithmetic.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Lexeme;
import ru.dronov.matlogic.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArithmeticParser extends Parser {

    public ArithmeticParser(String input) {
        lexemParser = new ArithmeticLexemeParser(input);
    }

    public ArithmeticParser() {
        super();
    }

    @Override
    public Expression parse(String input) throws IOException, ParserException {
        lexemParser = new ArithmeticLexemeParser(input);
        return parse();
    }

    @Override
    protected Predicate expressionPredicate() throws IOException {
        switch (getLexeme()) {
            case PREDICATE:
                return parsePredicate();
            default:
                Term left = expressionTerm();
                next();
                Term right = expressionTerm();
                return new Equals(left, right);
        }
    }

    private Predicate parsePredicate() throws IOException {
        String predicateName = getLexemeName();
        next();
        if (getLexeme() == Lexeme.LEFT_BRACKET) {
            List<Term> terms = new ArrayList<>();
            do {
                next();
                terms.add(expressionTerm());
            } while (getLexeme() == Lexeme.COMMA);
            next();
            return new Predicate(predicateName, terms);
        } else {
            return new Predicate(predicateName);
        }
    }

    private Term expressionTerm() throws IOException {
        Term term = expressionSum();
        while (getLexeme() == Lexeme.PLUS) {
            next();
            term = new Plus(Arrays.asList(term, expressionSum()));
        }
        return term;
    }

    private Term expressionSum() throws IOException {
        Term term = expressionMul();
        while (getLexeme() == Lexeme.MUL) {
            next();
            term = new Mul(Arrays.asList(term, expressionMul()));
        }
        return term;
    }

    private Term expressionMul() throws IOException {
        Term result;
        switch (getLexeme()) {
            case ZERO:
                next();
                result = new Zero();
                break;
            case LEFT_BRACKET:
            case SKIP:
                next();
                result = expressionTerm();
                next();
                break;
            default:
                String name = getLexemeName();
                next();
                if (getLexeme() == Lexeme.LEFT_BRACKET) {
                    List<Term> terms = new ArrayList<>();
                    do {
                        next();
                        terms.add(expressionTerm());
                    } while (getLexeme() == Lexeme.COMMA);
                    next();
                    result = new Term(name, terms);
                } else {
                    result = new Variable(name);
                }
                break;
        }

        while (getLexeme() == Lexeme.STROKE) {
            result = new Stroke(Collections.singletonList(result));
            next();
        }
        return result;
    }
    
}
