package ru.dronov.matlogic.exceptions;

import java.util.Map;

public class UntruthException extends Exception {

    private static final String MESSAGE = "Высказывание ложно при ";

    public UntruthException(Map<String, Boolean> values) {
        super(MESSAGE + generate(values));

    }
    private static String generate(Map<String, Boolean> values) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : values.entrySet()) {
            builder.append(entry.getKey()).append(" = ").append(entry.getValue() ? "И" : "Л").append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

}
