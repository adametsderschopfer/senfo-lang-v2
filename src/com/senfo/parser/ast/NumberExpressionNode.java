package com.senfo.parser.ast;

public final class NumberExpressionNode implements IExpressionNode {
    private final double value;

    public NumberExpressionNode(double value) {
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
