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
        StringBuilder builder = new StringBuilder();
        builder.append(getQuantifier());
        builder.append(term);
        builder.append("(");
        builder.append(argument);
        builder.append(")");
        return builder.toString();
    }

    protected abstract String getQuantifier();
}
