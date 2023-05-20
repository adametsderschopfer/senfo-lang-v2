package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;

public final class BinaryExpression implements IExpression {
    private final IExpression left, right;
    private final char operation;

    public BinaryExpression(char operation, IExpression left, IExpression right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public IValue eval() {
        final double exprLeft = left.eval().asDouble();
        final double exprRight = left.eval().asDouble();

        return switch (operation) {
            case '-' -> new NumberValue(exprLeft - exprRight);
            case '/' -> new NumberValue(exprLeft / exprRight);
            case '*' -> new NumberValue(exprLeft * exprRight);
            default -> new NumberValue(exprLeft + exprRight);
        };
    }

    @Override
    public String toString() {
        return String.format("[%s %c %s]", left, operation, right);
    }
}
