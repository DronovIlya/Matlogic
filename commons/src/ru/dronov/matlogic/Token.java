package ru.dronov.matlogic;

public enum Token {

    NOT("!"),
    AND("&"),
    OR("|"),
    IMPLICATION("->"),

    EXISTENCE("?"),
    UNIVERSAL("@"),
    DERIVABLE("|-"),

    TERM("x"),
    PREDICATE("P"),

    LEFT_BRACKET("("),
    RIGHT_BRACKET(")"),
    COMMA(","),

    NEW_LINE("\n"),
    END_FILE("-1");

    private final String value;

    Token(String value) {
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
