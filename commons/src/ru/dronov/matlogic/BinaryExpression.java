package ru.dronov.matlogic;

import java.util.HashSet;
import java.util.Set;

public abstract class BinaryExpression extends Expression {

    private static final String TAG = BinaryExpression.class.getName();

    public final Expression left;
    public final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Set<String> getFreeVariables() {
        Set<String> result = new HashSet<>();
        result.addAll(left.getFreeVariables());
        result.addAll(right.getFreeVariables());
        return result;
    }

    @Override
    public String toString() {
        return "(" + left + ")" + getSign() + "(" + right + ")";
    }

    protected abstract String getSign();

}
