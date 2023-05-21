package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;
import com.senfo.lib.StringValue;

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
        final IValue valueLeft = left.eval();
        final IValue valueRight = right.eval();

        if (valueLeft instanceof StringValue) {
            final String stringLeft = valueLeft.asString();

            if (operation == '*') {
                final int iterations = (int) valueRight.asDouble();
                return new StringValue(String.valueOf(stringLeft).repeat(Math.max(0, iterations)));
            }

            return new StringValue(stringLeft + valueRight.asString());
        }

        final double numberLeft = valueLeft.asDouble();
        final double numberRight = valueRight.asDouble();

        return switch (operation) {
            case '-' -> new NumberValue(numberLeft - numberRight);
            case '/' -> new NumberValue(numberLeft / numberRight);
            case '*' -> new NumberValue(numberLeft * numberRight);
            default -> new NumberValue(numberLeft + numberRight);
        };
    }

    @Override
    public String toString() {
        return String.format("[%s %c %s]", left, operation, right);
    }
}
