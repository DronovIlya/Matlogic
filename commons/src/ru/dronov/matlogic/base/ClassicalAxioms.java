package ru.dronov.matlogic.base;

import com.sun.istack.internal.Nullable;
import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.ClassicalParser;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.arithmetic.ArithmeticParser;
import ru.dronov.matlogic.parser.predicate.PredicateParser;

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

    public ClassicalAxioms(Type type) throws IOException, ParserException {
        axiomList = new ArrayList<>(AXIOM_LIST.length);
        for (String axiom : AXIOM_LIST) {
            axiomList.add(parseAxiom(axiom, type));
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

    private Expression parseAxiom(String axiom, Type type) throws IOException, ParserException {
        switch (type) {
            case CLASSICAL:
                return parseClassicalAxiom(axiom);
            case PREDICATE:
                return parsePredicateAxiom(axiom);
            case ARITHMETIC:
                return parseArithAxiom(axiom);
            default:
                throw new IllegalArgumentException("unknown type = " + type);
        }
    }

    private Expression parseClassicalAxiom(String axiom) throws IOException, ParserException {
        Parser parser = new ClassicalParser();
        return parser.parse(axiom);
    }

    private Expression parsePredicateAxiom(String axiom) throws IOException, ParserException {
        Parser parser = new PredicateParser();
        return parser.parse(axiom);
    }

    private Expression parseArithAxiom(String axiom) throws IOException, ParserException {
        Parser parser = new ArithmeticParser();
        return parser.parse(axiom);
    }

    public enum Type {
        CLASSICAL,
        PREDICATE,
        ARITHMETIC
    }
}
