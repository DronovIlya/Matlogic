package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Lexeme;

import java.util.List;

public class Mul extends ArithOperation {

    public Mul(List<Term> terms) {
        super(Lexeme.MUL.name(), terms);
    }

    @Override
    public String toString() {
        return terms.get(0) + Lexeme.MUL.getValue() + terms.get(1);
    }
}
