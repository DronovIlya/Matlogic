package ru.dronov.matlogic.model;

import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.base.Expression;
import ru.dronov.matlogic.model.base.UnaryExpression;
import ru.dronov.matlogic.parser.Token;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Negation extends UnaryExpression {

    public Negation(Expression expression) {
        super(expression);
    }

    @Override
    protected String getSign() {
        return Token.NOT.getValue();
    }

    @Override
    public boolean prove(Map<String, Boolean> values, List<Expression> current,
                         Set<String> dictionary) throws ResourceNotFound {
        if (dictionary.contains(this.toString()) ||
                dictionary.contains(new Negation(this).toString())) {
            return dictionary.contains(this.toString());
        }

        boolean value = argument.prove(values, current, dictionary);
        if (value) {
            current.addAll(Replacer.replaceNot(argument));
            dictionary.add(new Negation(this).toString());
            return false;
        } else {
            dictionary.add(this.toString());
            return true;
        }
    }
}
