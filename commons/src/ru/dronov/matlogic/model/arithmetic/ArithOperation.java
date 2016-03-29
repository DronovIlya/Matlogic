package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;

import java.util.List;

public abstract class ArithOperation extends Term {

    public ArithOperation(String name, List<Term> terms) {
        super(name, terms);
    }

}
