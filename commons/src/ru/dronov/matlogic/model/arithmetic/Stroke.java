package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Token;

import java.util.List;

public class Stroke extends ArithOperation {

    public Stroke(List<Term> terms) {
        super(Token.STROKE.name(), terms);
    }

    @Override
    public String toString() {
        return "(" + terms.get(0).toString() + ")" + Token.STROKE.getValue();
    }
}
