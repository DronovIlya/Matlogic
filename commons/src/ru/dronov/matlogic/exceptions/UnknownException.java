package ru.dronov.matlogic.exceptions;

public class UnknownException extends Exception {

    private static final String MESSAGE = "Неизвестная ошибка. Не получилось доказать : \"%s\"";

    public UnknownException(String message) {
        super(String.format(MESSAGE, message));
    }
}
