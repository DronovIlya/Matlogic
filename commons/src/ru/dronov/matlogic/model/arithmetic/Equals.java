package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Predicate;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Token;

import java.util.Arrays;
import java.util.List;

public class Equals extends Predicate {

    public Equals(Term left, Term right) {
        super(Token.EQUALS.name(), Arrays.asList(left, right));
    }

    @Override
    public String toString() {
        return (terms.get(0).toString() + Token.EQUALS.getValue() + terms.get(1).toString());
    }
}
