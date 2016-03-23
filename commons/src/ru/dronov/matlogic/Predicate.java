package ru.dronov.matlogic;

import java.util.List;
import java.util.Set;

public class Predicate extends Expression {

    private static final String TAG = Predicate.class.getName();

    public final String name;
    public final List<Term> terms;

    public Predicate(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    @Override
    public Set<String> getFreeVariables() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(name);
        if (terms != null && terms.size() > 0) {
            builder.append("(");
            for (Term term : terms) {
                builder.append(term).append(",");
            }
            builder.delete(builder.length() - 1, builder.length());
            builder.append(")");
        }
        return builder.toString();
    }
}
