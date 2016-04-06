package ru.dronov.matlogic.model.base;

import ru.dronov.matlogic.base.Replacer;
import ru.dronov.matlogic.model.predicate.Term;
import ru.dronov.matlogic.model.predicate.Variable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    public Set<Variable> getFreeVariables(Set<Variable> blocked) {
        Set<Variable> result = new HashSet<>();
        result.addAll(left.getFreeVariables(blocked));
        result.addAll(right.getFreeVariables(blocked));
        return result;
    }

    @Override
    public boolean compare(Expression expression, Map<Object, Object> dictionary) {
        return left.compareInternal(((BinaryExpression) expression).left, dictionary) &&
                right.compareInternal(((BinaryExpression) expression).right, dictionary);
    }

    @Override
    public boolean replace(Variable from, Variable to) {
        return left.replace(from, to) || right.replace(from, to);
    }

    @Override
    public boolean substitute(Variable from, Term to, Set<Variable> blocked) {
        return left.substitute(from, to, blocked) && right.substitute(from, to, blocked);
    }

    @Override
    public String toString() {
        return "(" + "(" + left + ")" + getOperator() + "(" + right + ")" + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        } else {
            return left.equals(((BinaryExpression) obj).left) && right.equals(((BinaryExpression) obj).right);
        }
    }

    @Override
    public boolean compareWithEquals(Expression expression, Variable variable, Map<Object, Object> dictionary) {
        if (expression.getClass() != getClass()) {
            return false;
        }
        return left.compareWithEquals(expression, variable, dictionary) &&
                right.compareWithEquals(expression, variable, dictionary);
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }

    /**
     * Used only for {@link #toString()}
     */
    protected abstract String getOperator();

}
