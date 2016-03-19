import ru.dronov.matlogic.Expression;
import ru.dronov.matlogic.parser.Parser;
import ru.dronov.matlogic.parser.PredicateParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Parser parser = new PredicateParser("input.txt");
        Expression expression = parser.parse();
        System.out.println(expression);
    }
}
