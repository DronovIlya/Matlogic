package ru.dronov.matlogic.parser.predicate;

import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Lexeme;
import ru.dronov.matlogic.parser.Parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PredicateParser extends Parser {

    public PredicateParser(String hypothesis) {
        super(hypothesis);
    }

    public PredicateParser() {
    }

    public HypothesisHolder parseHypothesis() throws IOException, ParserException {
        HypothesisHolder holder = null;
        Expression expression = parse();
        if (getLexeme() == Lexeme.COMMA || getLexeme() == Lexeme.DERIVABLE) {
            List<Expression> hypothesis = new ArrayList<>();
            hypothesis.add(expression);

            while (getLexeme() == Lexeme.COMMA) {
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
    protected Predicate expressionPredicate() throws IOException, ParserException {
        if (getLexeme() != Lexeme.PREDICATE) {
            throw new ParserException(getLexeme());
        }
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
        Term result;
        if (getLexeme() == Lexeme.LEFT_BRACKET) {
            next();
            result = expressionTerm();
        } else {
            String name = getLexemeName();
            List<Term> terms = new ArrayList<>();
            next();
            if (getLexeme() == Lexeme.LEFT_BRACKET) {
                do {
                    next();
                    terms.add(expressionTerm());
                } while (getLexeme() == Lexeme.COMMA);

                next();
                result = new Term(name, terms);
            } else {
                result = new Variable(name);
            }
        }
        return result;
    }
}
