package ru.dronov.matlogic.parser;

import ru.dronov.matlogic.Expression;

import java.util.List;

public class HypothesisHolder {

    public final List<Expression> hypothesis;
    public final Expression alpha;
    public final Expression beta;

    public HypothesisHolder(List<Expression> hypothesis, Expression alpha, Expression beta) {
        this.hypothesis = hypothesis;
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public String toString() {
        return "HypothesisHolder{" +
                "hypothesis=" + hypothesis +
                ", alpha=" + alpha +
                ", beta=" + beta +
                '}';
    }
}
