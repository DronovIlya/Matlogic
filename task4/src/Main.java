import ru.dronov.matlogic.Expression;
import ru.dronov.matlogic.parser.HypothesisHolder;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Main {

    private Parser parser;

    public Main() {
    }

    public void run() throws IOException {
        parser = new PredicateParser("input.txt");
        HypothesisHolder holder = parser.parseHypothesis();
        List<Expression> proof = readProof();

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
