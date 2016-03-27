package ru.dronov.matlogic.exceptions;

public class SubstitutionException extends Exception {

    private static final String MESSAGE = "переменная %s входит свободно в формулу %s";

    public SubstitutionException(String variable, String exception) {
        super(String.format(variable, exception));
    }
}
