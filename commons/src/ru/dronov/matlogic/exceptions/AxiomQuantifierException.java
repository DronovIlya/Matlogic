package ru.dronov.matlogic.exceptions;

public class AxiomQuantifierException extends Exception {

    private static final String MESSAGE = "используется схема аксиом с квантором по переменной \"%s\", " +
            "входящей свободно в допущение \"%s\"";

    public AxiomQuantifierException(String variable, String expression) {
        super(String.format(MESSAGE, variable, expression));
    }
}
