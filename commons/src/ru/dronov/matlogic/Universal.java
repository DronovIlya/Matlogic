package ru.dronov.matlogic;

public class Universal extends Quantifier {

    public Universal(Term term, Expression argument) {
        super(term, argument);
    }

    @Override
    protected String getQuantifier() {
        return Token.UNIVERSAL.getValue();
    }
}
