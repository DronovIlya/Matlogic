package ru.dronov.matlogic.model;

import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.base.Quantifier;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Lexeme;

public class Universal extends Quantifier {

    public Universal(Variable term, Expression argument) {
        super(term, argument);
    }

    @Override
    protected String getQuantifier() {
        return Lexeme.UNIVERSAL.getValue();
    }
}
