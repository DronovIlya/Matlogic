package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.parser.Token;

import java.util.ArrayList;
import java.util.List;

public class Zero extends ArithOperation {

    public Zero() {
        super(Token.ZERO.name(), new ArrayList<>());
    }

    @Override
    public String toString() {
        return Token.ZERO.getValue();
    }
}
