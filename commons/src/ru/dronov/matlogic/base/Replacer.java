package ru.dronov.matlogic.base;

import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.predicate.Variable;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;
import ru.dronov.matlogic.utils.Texts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Replacer {

    private static final String RESOURCE_DEFAULT_PATH = "/Users/ilya.dronov/ifmo/matlogic/matlogic_2.0/commons/src/ru/dronov/matlogic/base/resource/";

    public static List<Expression> replaceAimplA(Expression A) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "a_impl_a", A);
    }

    public static List<Expression> replaceAimplB(Expression A, Expression B) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "b_impl_a", A, B);
    }

    public static List<Expression> replaceAimplC(Expression A, Expression B, Expression C) throws ResourceNotFound {
        return replace("a_impl_c", A, B, C);
    }

    public static List<Expression> replaceUniversalModusPonens(Expression A, Expression B, Expression C, Variable x) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "universal_mp", A, B, C, x);
    }

    public static List<Expression> replaceExistanceModusPonens(Expression A, Expression B, Expression C, Variable x) throws ResourceNotFound {
        return replace(RESOURCE_DEFAULT_PATH + "existence_mp", A, B, C, x);
    }

    private static List<Expression> replace(String resource, Object... args) throws ResourceNotFound {
        try (BufferedReader reader = new BufferedReader(new FileReader(resource))) {
            String firstLine = reader.readLine();
            String[] variables = firstLine.split(" ");
            if (variables.length == 0) {
                throw new IllegalArgumentException("first line must contains of used variables");
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
                System.out.println("line = " + line);
                result.add(parseStringAxiom(line));
            }

            return result;

        } catch (IOException e) {
            throw new ResourceNotFound(resource);
        }
    }

    // TODO: remove it, rewrite parser
    private static Expression parseStringAxiom(String axiom) throws IOException {
        InputStream stream = new ByteArrayInputStream(axiom.getBytes());
        Parser parser = new PredicateParser(stream);
        return parser.parse();
    }
}
