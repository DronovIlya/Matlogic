package ru.dronov.matlogic.parser;

public enum Lexeme {

    NOT("!"),
    AND("&"),
    OR("|"),
    IMPLICATION("->"),

    EXISTENCE("?"),
    UNIVERSAL("@"),
    DERIVABLE("$"),

    TERM("x"),
    PREDICATE("P"),

    EQUALS("="),
    MUL("*"),
    PLUS("+"),
    ZERO("0"),
    STROKE("\'"),


    LEFT_BRACKET("("),
    RIGHT_BRACKET(")"),
    SKIP("#"),
    COMMA(","),

    NEW_LINE("\n"),
    END_LINE("-1");

    private final String value;

    Lexeme(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
