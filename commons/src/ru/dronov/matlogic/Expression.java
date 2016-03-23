package ru.dronov.matlogic;

import java.util.Set;

public abstract class Expression {

    public abstract Set<String> getFreeVariables();
}
