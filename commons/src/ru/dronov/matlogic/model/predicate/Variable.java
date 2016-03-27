package ru.dronov.matlogic.model.predicate;

import ru.dronov.matlogic.model.base.Expression;

import java.util.*;

public class Variable extends Term {

    private static final String TAG = Variable.class.getName();

    public Variable(String name) {
        super(name, new ArrayList<>());
    }

    @Override
    public boolean isSimilar(Expression expression, Map<String, Expression> dictionary) {
        if (dictionary.containsKey(name)) {
            return dictionary.get(name).equals(expression);
        } else {
            dictionary.put(name, expression);
            return true;
        }
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
    public boolean freeSubstitute(Variable from, Term to, Set<Variable> blocked) {
        if (this.equals(from)) {
            for (Variable variable : to.getVariables()) {
                if (blocked.contains(variable)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return name;
    }
}

