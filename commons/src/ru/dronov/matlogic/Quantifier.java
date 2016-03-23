package ru.dronov.matlogic;

import java.util.HashSet;
import java.util.Set;

public abstract class Quantifier extends Expression {

    private static final String TAG = Quantifier.class.getName();

    public final Term term;
    public final Expression argument;

    public Quantifier(Term term, Expression argument) {
        this.term = term;
        this.argument = argument;
    }

    @Override
    public Set<String> getFreeVariables() {
        Set<String> result = new HashSet<>();
        result.addAll(argument.getFreeVariables());
        result.remove(term.name);
        return result;
    }

    @Override
    public String toString() {
        return getQuantifier() + term + "(" + argument + ")";
    }

    protected abstract String getQuantifier();
}
