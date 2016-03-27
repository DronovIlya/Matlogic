package ru.dronov.matlogic.exceptions;

public class RuleQuantifierException extends Exception {

    private static final String MESSAGE = "используется правило %s с квантором по переменной %s, входящей свободно " +
            "в допущение %s";

    public RuleQuantifierException(String rule, String variable, String expression) {
        super(String.format(rule, variable, expression));
    }
}
