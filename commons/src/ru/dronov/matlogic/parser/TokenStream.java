package ru.dronov.matlogic.parser;

import java.io.*;

public class TokenStream {

    private static final String TAG = TokenStream.class.getName();

    private final PushbackInputStream stream;

    public TokenStream(String filename) throws FileNotFoundException {
        this(new FileInputStream(filename));
    }

    public TokenStream(InputStream stream) {
        this.stream = new PushbackInputStream(new BufferedInputStream(stream));
    }

    public Result next() throws IOException {
        Token result;
        String description = null;

        int token = stream.read();
        while (token == ' ') {
            token = stream.read();
        }

        if (token < 0) {
            result = Token.END_FILE;
        } else {
            switch (token) {
                case '?':
                    result = Token.EXISTENCE;
                    break;
                case '@':
                    result = Token.UNIVERSAL;
                    break;
                case '&':
                    result = Token.AND;
                    break;
                case '!':
                    result = Token.NOT;
                    break;
                case '=':
                    result = Token.EQUALS;
                    break;
                case '*':
                    result = Token.MUL;
                    break;
                case '+':
                    result = Token.PLUS;
                    break;
                case '0':
                    result = Token.ZERO;
                    break;
                case '\'':
                    result = Token.STROKE;
                    break;
                case '\n':
                    result = Token.NEW_LINE;
                    break;
                case ',':
                    result = Token.COMMA;
                    break;
                case '|':
                    token = stream.read();
                    if (token == '-') {
                        result = Token.DERIVABLE;
                        break;
                    } else {
                        stream.unread(token);
                    }
                    result = Token.OR;
                    break;
                case '-':
                    stream.read();
                    result = Token.IMPLICATION;
                    break;
                case '(':
                    result = Token.LEFT_BRACKET;
                    break;
                case ')':
                    result = Token.RIGHT_BRACKET;
                    break;
                case '[':
                    result = Token.LEFT_BRACKET_SQUARE;
                    break;
                case ']':
                    result = Token.RIGHT_BRACKET_SQUARE;
                    break;
                default:
                    if (token >= 'a' && token <= 'z') {
                        result = Token.TERM;
                    } else {
                        result = Token.PREDICATE;
                    }
                    int first = token;
                    token = stream.read();
                    String variable = new String(new char[]{(char) first});
                    while (token >= '0' && token <= '9') {
                        variable += (char) token;
                        token = stream.read();
                    }
                    if (token != -1) {
                        stream.unread(token);
                    }
                    description = variable;
                    break;
            }
        }
        return new Result(result, description);
    }

    public class Result {

        public final Token token;
        public final String description;

        public Result(Token token, String description) {
            this.token = token;
            this.description = description;
        }
    }
}
