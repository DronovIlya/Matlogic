package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Lexeme;

import java.util.List;

public class Stroke extends ArithOperation {

    public Stroke(List<Term> terms) {
        super(Lexeme.STROKE.name(), terms);
    }

    @Override
    public String toString() {
        return "(" + terms.get(0).toString() + ")" + Lexeme.STROKE.getValue();
    }
}
