package ru.dronov.matlogic.model.predicate;

import ru.dronov.matlogic.model.base.Expression;

import java.util.*;

public class Term extends Predicate {

    private static final String TAG = Term.class.getName();

    public Term(String name) {
        super(name);
    }

    public Term(String name, List<Term> terms) {
        super(name, terms);
    }

    public Set<Variable> getVariables() {
        Set<Variable> result = new HashSet<>();
        for (Term term : terms) {
            result.addAll(term.getVariables());
        }
        return result;
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
    public boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary) {
        if (Term.class != expression.getClass()) {
            return false;
        }
        Term term = (Term) expression;
        if (!term.name.equals(name) || term.terms.size() != terms.size()) {
            return false;
        }
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).compareWithEquals(term.terms.get(i), variable, dictionary)) {
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
