package ru.dronov.matlogic.model;

import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.ParserException;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.base.BinaryExpression;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.parser.Lexeme;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Implication extends BinaryExpression {

    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    protected String getOperator() {
        return Lexeme.IMPLICATION.getValue();
    }

    @Override
    public boolean prove(Map<String, Boolean> values, List<Expression> current,
                         Set<String> dictionary) throws ResourceNotFound, ParserException {
        if (dictionary.contains(this.toString()) ||
                dictionary.contains(new Negation(this).toString())) {
            return dictionary.contains(this.toString());
        }

        boolean leftValue = left.prove(values, current, dictionary);
        boolean rightValue = right.prove(values, current, dictionary);

        current.addAll(Replacer.replaceImpl(left, right, leftValue, rightValue));

        dictionary.add(current.get(current.size() - 1).toString());
        return current.get(current.size() - 1).equals(this);
    }
}
