package ru.dronov.matlogic.model.predicate;

import ru.dronov.matlogic.model.base.Expression;

import java.util.*;

public class Term {

    private static final String TAG = Term.class.getName();

    public String name;
    public final List<Term> terms;

    public Term(String name, List<Term> terms) {
        this.name = name;
        this.terms = terms;
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<>();
        for (Term term : terms) {
            result.addAll(term.getVariables());
        }
        return result;
    }

    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        Set<Variable> result = new HashSet<>();
        for (Term term : terms) {
            result.addAll(term.getFreeVariables(blocked));
        }
        return result;
    }

    public boolean isSimilar(Term term, Map<Object, Object> dictionary) {
        boolean result = true;
        if (!term.name.equals(name)) {
            return false;
        }

        if (terms.size() != term.terms.size()) {
            result = false;
        } else {
            for (int i = 0; i < terms.size(); i++) {
                if (!terms.get(i).isSimilar(term.terms.get(i), dictionary)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public boolean replace(Variable from, Variable to) {
        boolean result = false;
        for (Term term : terms) {
            if (term.replace(from, to)) {
                result = true;
            }
        }
        return result;
    }

    public boolean freeSubstitute(Variable from, Term to, Set<Variable> blocked) {
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
        Term term = (Term) obj;
        if (!term.name.equals(name) || term.terms.size() != terms.size()) {
            return false;
        }
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).equals(term.terms.get(i))) {
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
