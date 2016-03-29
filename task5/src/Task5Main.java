import ru.dronov.matlogic.exceptions.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.ArithmeticParser;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;
import ru.dronov.matlogic.utils.Texts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Task5Main {

    public static final boolean DEBUG = false;

    private static final String BASE_ERROR_MESSAGE = "Вывод некорректен, начиная с формулы номер %d";
    private static final String BASE_SUCCESS_MESSAGE = "Доказательноство корректно";

    private static final String INPUT_FILE_SUFFIX = ".in";
    private static final String OUTPUT_FILE_SUFFIX = ".out";
    private static final String USAGE = "Usage: [option] [filepath]\n" +
            "-test input.in = read one sample test from input.in\n" +
            "-dir  dirname  = read and process all tests from given dir\n" +
            "IMPORTANT: filename must be ended with .in";

    private Parser parser;
    public ArithmeticHelper helper;

    public Task5Main() {
    }

    public void solver(String inputFile) throws IOException, ResourceNotFound, AxiomQuantifierException, TermSubstituteException, UnknownException, RuleQuantifierException, SubstitutionException {
        if (Texts.isEmpty(inputFile)) {
            throw new IllegalArgumentException("inputFile can't be empty");
        }
        parser = new ArithmeticParser(inputFile);
        List<Expression> proof = readProof();

        helper = new ArithmeticHelper();
        helper.handle(proof);
    }

    private List<Expression> readProof() throws IOException {
        List<Expression> result = new ArrayList<>();
        while (true) {
            Expression expression = parser.parse();
            if (expression == null) {
                break;
            }
            result.add(expression);
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.out.println(USAGE);
            return;
        }

        if ("-test".equals(args[0])) {
            solveOneTest(args[1]);
        } else if ("-dir".equals(args[0])) {
            solveSeveralTest(args[1]);
        } else {
            System.out.println(USAGE);
        }
    }

    private static void solveOneTest(String fileName) throws FileNotFoundException {
        if (!fileName.endsWith(INPUT_FILE_SUFFIX)) {
            throw new IllegalArgumentException("fileName must be ended with suffix .in");
        }
        System.out.println("processing file : " + fileName);
        Task5Main solver = new Task5Main();
        PrintWriter writer = new PrintWriter(fileName.replace(INPUT_FILE_SUFFIX, OUTPUT_FILE_SUFFIX));
        try {
            solver.solver(fileName);
            writer.println(BASE_SUCCESS_MESSAGE);
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println(String.format(BASE_ERROR_MESSAGE, solver.helper.processedLines));
                e.printStackTrace();
            }
            writer.println(String.format(BASE_ERROR_MESSAGE, solver.helper.processedLines));
            if (!Texts.isEmpty(e.getMessage())) {
                writer.println(e.getMessage());
            }
        } finally {
            writer.close();
        }
    }

    private static void solveSeveralTest(String directoryName) throws FileNotFoundException {
        File folder = new File(directoryName);
        File[] files = folder.listFiles();
        if (files != null ) {
            for (File file : files) {
                if (file.getName().endsWith(INPUT_FILE_SUFFIX)) {
                    solveOneTest(file.getAbsolutePath());
                }
            }
        }
    }
}
