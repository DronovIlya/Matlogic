package ru.dronov.matlogic.model.predicate;

import ru.dronov.matlogic.exceptions.ResourceNotFound;
import ru.dronov.matlogic.model.Negation;
import ru.dronov.matlogic.model.base.Expression;

import java.util.*;

public class Variable extends Term {

    private static final String TAG = Variable.class.getName();

    public Variable(String name) {
        super(name, new ArrayList<>());
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        if (name.equals(from.name)) {
            name = to.name;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        Set<Variable> result = new HashSet<>();
        if (!blocked.contains(this)) {
            result.add(this);
        }
        return result;
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        if (this.equals(from)) {
            if (to == null) {
                System.out.println("OMG!!!!");
            } else {
                for (Variable variable : to.getVariables()) {
                    if (blocked.contains(variable)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary) {
        if (expression instanceof Term && name.equals(variable.name)) {
            if (!dictionary.containsKey(name)) {
                dictionary.put(name, expression);
                return true;
            } else {
                return dictionary.get(name).equals(expression);
            }
        }
        if (expression.getClass() != Variable.class) {
            return false;
        }
        return ((Variable)expression).name.equals(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) {
            return false;
        }
        return ((Variable)obj).name.equals(name);
    }

    @Override
    public boolean prove(Map<String, Boolean> values, List<Expression> current,
                         Set<String> dictionary) throws ResourceNotFound {
        if (dictionary.contains(this.toString()) ||
                dictionary.contains(new Negation(this).toString())) {
            return dictionary.contains(this.toString());
        }

        boolean result = values.get(name);
        if (result) {
            current.add(this);
            dictionary.add(this.toString());
            return true;
        } else {
            current.add(new Negation(this));
            dictionary.add(new Negation(this).toString());
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }


}

