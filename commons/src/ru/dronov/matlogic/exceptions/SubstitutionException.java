package ru.dronov.matlogic.exceptions;

public class SubstitutionException extends Exception {

    private static final String MESSAGE = "переменная %\"%s\" входит свободно в формулу ";

    public SubstitutionException(String variable, String exception) {
        super("переменная " + "\"" + variable + "\"" + " входит свободно в формулу : " + "\"" + exception + "\"");
    }
}
