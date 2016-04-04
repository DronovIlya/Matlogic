package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.utils.Texts;

public class TokenParser {

    private final String input;

    private int currentIndex = -1;

    public Token currentTokenType;
    public String currentTokenName;

    public TokenParser(String input) {
        this.input = input.replace("|-", "$").replace("->", "-");
    }

    public void nextToken() {
        currentTokenType = parseNextToken();
        while (currentTokenType == Token.NEW_LINE) {
            currentTokenType = parseNextToken();
        }
    }

    private Token parseNextToken() {
        char ch = nextChar();
        if (currentIndex == input.length()) {
            return Token.END_LINE;
        } else {
            switch (ch) {
                case '!':
                    return Token.NOT;
                case '&':
                    return Token.AND;
                case '|':
                    return Token.OR;
                case '-':
                    return Token.IMPLICATION;
                case '?':
                    return Token.EXISTENCE;
                case '@':
                    return Token.UNIVERSAL;
                case '$':
                    return Token.DERIVABLE;
                case '=':
                    return Token.EQUALS;
                case '*':
                    return Token.MUL;
                case '+':
                    return Token.PLUS;
                case '0':
                    return Token.ZERO;
                case '\'':
                    return Token.STROKE;
                case '(':
                    return Token.LEFT_BRACKET;
                case '[':
                    return Token.LEFT_BRACKET_SQUARE;
                case ')':
                    return Token.RIGHT_BRACKET;
                case ']':
                    return Token.RIGHT_BRACKET_SQUARE;
                case ',':
                    return Token.COMMA;
                case '\n':
                    return Token.NEW_LINE;
                default:
                    Token result = Texts.isLower(ch) ? Token.TERM : Token.PREDICATE;
                    String name = new String(new char[]{ch});

                    ch = nextChar();
                    while (currentIndex < input.length() && ch != 0 && Texts.isDigit(ch)) {
                        name += ch;
                        ch = nextChar();
                    }

                    if (currentIndex != input.length()) {
                        currentIndex--;
                    }
                    this.currentTokenName = name;
                    return result;
            }
        }
    }

    protected char nextChar() {
        char result = 0;
        currentIndex++;
        if (currentIndex < input.length()) {
            result = input.charAt(currentIndex);
        }
        return result;
    }
}
