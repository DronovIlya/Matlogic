package ru.dronov.matlogic;

import java.util.ArrayList;
import java.util.List;

public class Variable extends Predicate {

    private static final String TAG = Variable.class.getName();

    public Variable(String name) {
        super(name, new ArrayList<>());
    }

    @Override
    public String toString() {
        return name;
    }
}

