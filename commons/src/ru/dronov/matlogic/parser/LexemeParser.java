package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.utils.Texts;

public class LexemeParser {

    private final String input;

    private int currentIndex = -1;

    public Lexeme type;
    public String lexemeName;

    public LexemeParser(String input) {
        this.input = input.replace("|-", "$").replace("->", "-");
    }

    public void nextToken() {
        type = parseNextToken();
        while (type == Lexeme.NEW_LINE) {
            type = parseNextToken();
        }
    }

    private Lexeme parseNextToken() {
        int ch = nextChar();
        if (ch == -1) {
            return Lexeme.END_LINE;
        } else {
            switch (ch) {
                case '!':
                    return Lexeme.NOT;
                case '&':
                    return Lexeme.AND;
                case '|':
                    return Lexeme.OR;
                case '-':
                    return Lexeme.IMPLICATION;
                case '?':
                    return Lexeme.EXISTENCE;
                case '@':
                    return Lexeme.UNIVERSAL;
                case '$':
                    return Lexeme.DERIVABLE;
                case '=':
                    return Lexeme.EQUALS;
                case '*':
                    return Lexeme.MUL;
                case '+':
                    return Lexeme.PLUS;
                case '0':
                    return Lexeme.ZERO;
                case '\'':
                    return Lexeme.STROKE;
                case '(':
                    return Lexeme.LEFT_BRACKET;
                case '#':
                    return Lexeme.SKIP;
                case ')':
                    return Lexeme.RIGHT_BRACKET;
                case ',':
                    return Lexeme.COMMA;
                case '\n':
                    return Lexeme.NEW_LINE;
                default:
                    Lexeme result = Texts.isLower(ch) ? Lexeme.TERM : Lexeme.PREDICATE;
                    String name = new String(new char[]{(char)ch});

                    ch = nextChar();
                    while (ch != -1 && Texts.isDigit(ch)) {
                        name += (ch - '0');
                        ch = nextChar();
                    }

                    if (ch != -1) {
                        decChar();
                    }
                    this.lexemeName = name;
                    return result;
            }
        }
    }

    protected int nextChar() {
        int result = -1;
        currentIndex++;
        if (currentIndex < input.length()) {
            result = input.charAt(currentIndex);
        }
        return result;
    }

    protected void decChar() {
        if (currentIndex > 0) {
            currentIndex--;
        }
    }
}
