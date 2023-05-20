package com.senfo.parser.ast;

public final class NumberExpression implements IExpression {
    private final double value;

    public NumberExpression(double value) {
        this.value = value;
    }

    @Override
    public double eval() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
