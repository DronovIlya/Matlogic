import ru.dronov.matlogic.exceptions.*;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.HypothesisHolder;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;
import ru.dronov.matlogic.utils.Texts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final boolean DEBUG = false;

    private static final String BASE_ERROR_MESSAGE = "Вывод некорректен, начиная с формулы номер %d";

    private static final String INPUT_FILE_SUFFIX = ".in";
    private static final String OUTPUT_FILE_SUFFIX = ".out";

    private Parser parser;
    public PredicateHelper helper;

    public Main() {
    }

    public List<Expression> solver(String inputFile) throws IOException, ResourceNotFound, AxiomQuantifierException, TermSubstituteException, UnknownException, RuleQuantifierException, SubstitutionException {
        if (Texts.isEmpty(inputFile)) {
            throw new IllegalArgumentException("inputFile can't be empty");
        }
        parser = new PredicateParser(inputFile);
        HypothesisHolder holder = parser.parseHypothesis();
        List<Expression> proof = readProof();

        helper = new PredicateHelper(holder);
        return helper.handle(proof);
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
            System.out.println("USAGE");
            return;
        }

        if ("-test".equals(args[0])) {
            solveOneTest(args[1]);
        } else if ("-dir".equals(args[0])) {
            solveSeveralTest(args[1]);
        } else {
            System.out.println("USAGE");
        }
    }

    private static void solveOneTest(String fileName) throws FileNotFoundException {
        if (!fileName.endsWith(INPUT_FILE_SUFFIX)) {
            throw new IllegalArgumentException("fileName must be ended with suffix .in");
        }
        System.out.println("processing file : " + fileName);
        Main solver = new Main();
        PrintWriter writer = new PrintWriter(fileName.replace(INPUT_FILE_SUFFIX, OUTPUT_FILE_SUFFIX));
        try {
            List<Expression> result = solver.solver(fileName);

            for (Expression expression : result) {
                writer.println(expression);
            }
        } catch (Exception e) {
            if (DEBUG) {
                System.out.println(String.format(BASE_ERROR_MESSAGE, solver.helper.processedLines));
                e.printStackTrace();
            }
            writer.println(String.format(BASE_ERROR_MESSAGE, solver.helper.processedLines));
            writer.println(e.getMessage());
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
