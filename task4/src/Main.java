import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.exceptions.RuleQuantifierException;
import ru.dronov.matlogic.exceptions.SubstitutionException;
import ru.dronov.matlogic.exceptions.UnknownException;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.HypothesisHolder;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String BASE_ERROR_MESSAGE = "Вывод некорректен, начиная с формулы номер %d";

    private Parser parser;
    private PredicateHelper helper;

    public Main() {
    }

    public void run() throws IOException {
        parser = new PredicateParser("input.txt");
        HypothesisHolder holder = parser.parseHypothesis();
        List<Expression> proof = readProof();

        helper = new PredicateHelper(holder);
        try {
            helper.handle(proof);
        } catch (Exception e) {
            System.out.println(String.format(BASE_ERROR_MESSAGE, helper.processedLines));
            System.out.println(e.getMessage());
        }
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

    public static void main(String[] args) throws IOException {
        new Main().run();
    }
}
