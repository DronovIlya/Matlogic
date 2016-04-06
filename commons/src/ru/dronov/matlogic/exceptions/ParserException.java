package ru.dronov.matlogic.exceptions;

import ru.dronov.matlogic.parser.Lexeme;

public class ParserException extends Exception {

    public ParserException(Lexeme token) {
        super("Can not parse lexeme = " + token.getValue());
    }
}
