package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.List;
import java.util.Map;

public abstract class ArithOperation extends Term {

    public ArithOperation(String name, List<Term> terms) {
        super(name, terms);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ArithOperation)) {
            return false;
        }
        ArithOperation term = (ArithOperation) obj;
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
        if (!(expression instanceof ArithOperation)) {
            return false;
        }
        ArithOperation predicate = (ArithOperation) expression;
        if (!predicate.name.equals(name) || predicate.terms.size() != terms.size()) {
            return false;
        }
        for (int i = 0; i < terms.size(); i++) {
            if (!terms.get(i).compareWithEquals(predicate.terms.get(i), variable, dictionary)) {
                return false;
            }
        }
        return true;
    }

}
