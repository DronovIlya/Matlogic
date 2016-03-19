package ru.dronov.matlogic;

public abstract class BinaryExpression extends Expression {

    private static final String TAG = BinaryExpression.class.getName();

    public final Expression left;
    public final Expression right;

    public BinaryExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }


    @Override
    public String toString() {
        return "(" + left + ")" + getSign() + "(" + right + ")";
    }

    protected abstract String getSign();

}
