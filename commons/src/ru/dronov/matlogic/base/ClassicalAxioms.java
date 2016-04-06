package ru.dronov.matlogic.base;

import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.ArithmeticParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassicalAxioms {

    private final static String[] AXIOM_LIST = new String[] {
            "A->B->A",
            "(A->B)->((A->B->C)->(A->C))",
            "A->B->A&B",
            "A&B->A",
            "A&B->B",
            "A->A|B",
            "B->A|B",
            "(A->C)->((B->C)->((A|B)->C))",
            "(A->B)->(A->!B)->!A",
            "!!A->A"
    };

    private final List<Expression> axiomList;

    public ClassicalAxioms() throws IOException {
        axiomList = new ArrayList<>(AXIOM_LIST.length);
        for (String axiom : AXIOM_LIST) {
            axiomList.add(parseStringAxiom(axiom));
        }
    }

    @Nullable
    public Expression handle(Expression expression) {
        for (Expression axiom : axiomList) {
            if (expression.compare(axiom)) {
                return axiom;
            }
        }
        return null;
    }

    private Expression parseStringAxiom(String axiom) throws IOException {
        ArithmeticParser parser = new ArithmeticParser();
        return parser.parse(axiom);
    }
}
