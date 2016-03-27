package ru.dronov.matlogic.exceptions;

public class ResourceNotFound extends Exception {

    private static final String MESSAGE = "Нет ресурса по заданному пути : %s";

    public ResourceNotFound(String path) {
        super(String.format(MESSAGE, path));
    }
}
