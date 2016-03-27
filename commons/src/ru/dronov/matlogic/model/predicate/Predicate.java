package ru.dronov.matlogic.model.predicate;

import ru.dronov.matlogic.model.base.Expression;

import java.util.*;

public class Predicate extends Expression {

    private static final String TAG = Predicate.class.getName();

    public String name;
    public final List<Term> terms;

    public Predicate(String name) {
        this(name, new ArrayList<>());
    }

    public Predicate(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    @Override
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        Set<Variable> result = new HashSet<>();
        if (!terms.isEmpty()) {
            for (Term term : terms) {
                result.addAll(term.getFreeVariables(blocked));
            }
        }
        return result;
    }

    @Override
    public boolean compare(Expression expression, Map<Object, Object> dictionary) {
        boolean result = true;
        if (dictionary.containsKey(name)) {
            result = dictionary.get(name).equals(expression);
            if (!result) {
                return false;
            }
        } else {
            result = true;
            dictionary.put(name, expression);
        }

        if (expression instanceof Predicate) {
            Predicate predicate = (Predicate) expression;
            if (!terms.isEmpty() && !predicate.terms.isEmpty()) {
                if (terms.size() != predicate.terms.size()) {
                    result = false;
                } else {
                    for (int i = 0; i < terms.size(); i++) {
                        if (!terms.get(i).isSimilar(predicate.terms.get(i), dictionary)) {
                            result = false;
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        boolean result = false;
        for (Term term : terms) {
            if (term.replace(from, to)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        boolean result = true;
        for (Term term : terms) {
            if (!term.freeSubstitute(from, to, blocked)) {
                result = false;
                break;
            }
        }
        return result;
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

    @Override
    public boolean equals(Object obj) {
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Predicate predicate = (Predicate) obj;
        if (!predicate.name.equals(name) || predicate.terms.size() != terms.size()) {
            return false;
        }
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).equals(predicate.terms.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }
}
