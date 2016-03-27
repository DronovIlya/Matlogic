package ru.dronov.matlogic.model;

import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.base.Quantifier;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Token;

public class Existence extends Quantifier {

    private static final String TAG = Existence.class.getName();

    public Existence(Variable term, Expression argument) {
        super(term, argument);
    }

    @Override
    protected String getQuantifier() {
        return Token.EXISTENCE.getValue();
    }


}
