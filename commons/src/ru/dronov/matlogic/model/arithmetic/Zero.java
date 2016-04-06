package ru.dronov.matlogic.model.arithmetic;

import ru.dronov.matlogic.parser.Lexeme;

import java.util.ArrayList;

public class Zero extends ArithOperation {

    public Zero() {
        super(Lexeme.ZERO.name(), new ArrayList<>());
    }

    @Override
    public String toString() {
        return Lexeme.ZERO.getValue();
    }
}
