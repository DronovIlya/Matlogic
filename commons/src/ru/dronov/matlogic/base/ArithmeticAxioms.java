package ru.dronov.matlogic.base;

import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.ArithmeticParserNew;
import ru.dronov.matlogic.parser.ParserNew;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticAxioms {

    private final static String[] AXIOM_LIST = new String[] {
            "a=b->a'=b'",
            "a=b->a=c->b=c",
            "a'=b'->a=b",
            "!a'=0",
            "a+b'=(a+b)'",
            "a+0=a",
            "a*0=0",
            "a*b'=a*b+a"
    };

    private final List<Expression> axiomList;

    public ArithmeticAxioms() throws IOException {
        axiomList = new ArrayList<>(AXIOM_LIST.length);
        for (String axiom : AXIOM_LIST) {
            axiomList.add(parseStringAxiom(axiom));
        }
    }

    @Nullable
    public Expression handle(Expression expression) {
        for (Expression axiom : axiomList) {
            if (expression.equals(axiom)) {
                return axiom;
            }
        }
        return null;
    }

    private Expression parseStringAxiom(String axiom) throws IOException {
        ParserNew parser = new ArithmeticParserNew();
        return parser.parse(axiom);
    }

}
