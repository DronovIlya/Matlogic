package ru.dronov.matlogic;

public class Existance extends Quantifier {

    private static final String TAG = Existance.class.getName();

    public Existance(Term term, Expression argument) {
        super(term, argument);
    }

    @Override
    protected String getQuantifier() {
        return Token.EXISTENCE.getValue();
    }


}
