package ru.dronov.matlogic.base;

import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.ClassicalParser;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;
import ru.dronov.matlogic.parser.PredicateParserNew;
import ru.dronov.matlogic.utils.Texts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Replacer {

    private static final String RESOURCE_DEFAULT_PATH = "/Users/ilya.dronov/ifmo/matlogic/matlogic_2.0/commons/src/ru/dronov/matlogic/base/resource/";

    public static List<Expression> replaceAnd(Expression A, Expression B, boolean a, boolean b) throws ResourceNotFound {
        String path = RESOURCE_DEFAULT_PATH + "and/";
        String suffix = (a ? "" : "not_") + "a" + "_and_" + (b ? "" : "not_") + "b";
        return replaceExpressions(path + suffix, true, A, B);
    }

    public static List<Expression> replaceImpl(Expression A, Expression B, boolean a, boolean b) throws ResourceNotFound {
        String path = RESOURCE_DEFAULT_PATH + "implication/";
        String suffix = (a ? "" : "not_") + "a" + "_impl_" + (b ? "" : "not_") + "b";
        return replaceExpressions(path + suffix, true, A, B);
    }

    public static List<Expression> replaceOr(Expression A, Expression B, boolean a, boolean b) throws ResourceNotFound {
        String path = RESOURCE_DEFAULT_PATH + "or/";
        String suffix = (a ? "" : "not_") + "a" + "_or_" + (b ? "" : "not_") + "b";
        return replaceExpressions(path + suffix, true, A, B);
    }

    public static List<Expression> replaceNot(Expression A) throws ResourceNotFound {
        return replaceExpressions(RESOURCE_DEFAULT_PATH + "not/not_not_a", true, A);
    }

    public static List<Expression> replaceANotA_B(Expression A, Expression B) throws ResourceNotFound {
        return replaceExpressions(RESOURCE_DEFAULT_PATH + "a_not_a_b", true, A, B);
    }

    public static List<Expression> replaceAimplA(Expression A) throws ResourceNotFound {
        return replaceExpressions(RESOURCE_DEFAULT_PATH + "a_impl_a", true, A);
    }

    public static List<Expression> replaceAimplAPredicate(Expression A) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "a_impl_a", A);
    }

    public static List<Expression> replaceAimplB(Expression A, Expression B) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "b_impl_a", A, B);
    }

    public static List<Expression> replaceAimplC(Expression A, Expression B, Expression C) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "a_impl_c", A, B, C);
    }

    public static List<Expression> replaceUniversalModusPonens(Expression A, Expression B, Expression C, Variable x) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "universal_mp", A, B, C, x);
    }

    public static List<Expression> replaceExistanceModusPonens(Expression A, Expression B, Expression C, Variable x) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "existence_mp", A, B, C, x);
    }

    private static List<Expression> replace(String resource, Object... args) throws ResourceNotFound {
        return replaceExpressions(resource, false, args);
    }

    private static List<Expression> replaceExpressions(String resource, boolean useClassicalParser, Object... args) throws ResourceNotFound {
        try (BufferedReader reader = new BufferedReader(new FileReader(resource))) {
            String firstLine = reader.readLine();
            String[] variables = firstLine.split(" ");
            if (variables.length == 0) {
                throw new IllegalArgumentException("first line must contains used variables");
            }
            if (variables.length != args.length) {
                throw new IllegalArgumentException("you must use the same number of variables and expressions");
            }

            List<Expression> result = new ArrayList<>();
            while (true) {
                String line = reader.readLine();
                if (Texts.isEmpty(line)) {
                    break;
                }
                for (int i = 0; i < variables.length; i++) {
                    if (args[i] != null) {
                        line = line.replace(variables[i], args[i].toString());
                    }
                }
                result.add(parseExpression(line, useClassicalParser));
            }

            return result;

        } catch (IOException e) {
            throw new ResourceNotFound(resource);
        }
    }

    private static Expression parseExpression(String expression, boolean useClassicalParser) throws IOException {
        if (useClassicalParser) {
            return parseClasicalAxiom(expression);
        } else {
            return parsePredicateAxiom(expression);
        }
    }

    private static Expression parsePredicateAxiom(String axiom) throws IOException {
        PredicateParserNew parser = new PredicateParserNew();
        return parser.parse(axiom);
    }

    private static Expression parseClasicalAxiom(String axiom) throws IOException {
        ClassicalParser parser = new ClassicalParser();
        return parser.parse(axiom);
    }
}
