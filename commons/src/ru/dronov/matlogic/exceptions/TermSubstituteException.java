package ru.dronov.matlogic.exceptions;

public class TermSubstituteException extends Exception {

    private static final String MESSAGE = "терм \"%s\" не сводобен для подстановки в формулу \"%s\" вместо переменной \"%s\"";

    public TermSubstituteException(String term, String expression, String variable) {
        super(String.format(MESSAGE, term, expression, variable));
    }
}
