package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.exceptions.ParserException;
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
    protected Expression expressionPredicate() throws IOException, ParserException {
        if (getLexeme() == Lexeme.PREDICATE || getLexeme() == Lexeme.TERM) {
            String name = getLexemeName();
            next();
            return new Variable(name);
        }
        throw new ParserException(getLexeme());
    }
}
