package ru.dronov.matlogic;

public abstract class Quantifier extends Expression {

    private static final String TAG = Quantifier.class.getName();

    public final Term term;
    public final Expression argument;

    public Quantifier(Term term, Expression argument) {
        this.term = term;
        this.argument = argument;
    }

    @Override
    public String toString() {
        return getQuantifier() + term + "(" + argument + ")";
    }

    protected abstract String getQuantifier();
}
