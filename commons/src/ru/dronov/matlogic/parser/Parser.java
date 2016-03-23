package ru.dronov.matlogic.parser;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.Expression;
import ru.dronov.matlogic.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class Parser {

    protected final TokenStream stream;
    protected TokenStream.Result token;

    protected Parser(String filename) throws FileNotFoundException {
        stream = new TokenStream(filename);
    }

    protected Parser(InputStream in) {
        stream = new TokenStream(in);
    }

    protected void nextToken() throws IOException {
        token = stream.next();
    }

    @Nullable
    protected Token getToken() {
        if (token != null) {
            return token.token;
        } else {
            return null;
        }
    }

    @Nullable
    protected String getDescription() {
        if (token != null) {
            return token.description;
        } else {
            return null;
        }
    }

    @NotNull
    public abstract HypothesisHolder parseHypothesis() throws IOException;

    @Nullable
    public abstract Expression parse() throws IOException;
}
