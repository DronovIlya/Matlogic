package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Lexeme;

import java.util.List;

public class Plus extends ArithOperation {

    public Plus(List<Term> terms) {
        super(Lexeme.PLUS.name(), terms);
    }

    @Override
    public String toString() {
        return terms.get(0) + Lexeme.PLUS.getValue() + terms.get(1);

    }
}
