package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Equals extends Predicate {

    public Equals(Term left, Term right) {
        super(Token.EQUALS.name(), Arrays.asList(left, right));
    }

    @Override
    public boolean equals(Object obj) {
        if (Equals.class != obj.getClass()) {
            return false;
        }
        Equals term = (Equals) obj;
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
        if (Equals.class != expression.getClass()) {
            return false;
        }
        Predicate predicate = (Predicate) expression;
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


    @Override
    public String toString() {
        return (terms.get(0).toString() + Token.EQUALS.getValue() + terms.get(1).toString());
    }
}
