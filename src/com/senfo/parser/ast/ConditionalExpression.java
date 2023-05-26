package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;
import com.senfo.lib.StringValue;

public final class ConditionalExpression implements IExpression {
    public static enum Operator {
        PLUS("+"),
        MINUS("-"),
        MULTIPLY("*"),
        DIVIDE("/"),

        EQUALS("=="),
        NOT_EQUALS("!="),
        LT("<"),
        LTEQ("<="),
        GT(">"),
        GTEQ(">="),

        AND("&&"),
        OR("||");

        private final String name;

        private Operator(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final IExpression left, right;
    private final Operator operation;

    public ConditionalExpression(Operator operation, IExpression left, IExpression right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public IValue eval() {
        final IValue valueLeft = left.eval();
        final IValue valueRight = right.eval();

        double numberLeft, numberRight;
        if (valueLeft instanceof StringValue) {
            numberLeft = valueLeft.asString().compareTo(valueRight.asString());
            numberRight = 0;
        } else {
            numberLeft = valueLeft.asNumber();
            numberRight = valueRight.asNumber();
        }

        return new NumberValue(switch (operation) {
            case LT -> numberLeft < numberRight;
            case LTEQ -> numberLeft <= numberRight;
            case GT -> numberLeft > numberRight;
            case GTEQ -> numberLeft >= numberRight;

            case NOT_EQUALS -> numberLeft != numberRight;

            case AND -> (numberLeft != 0) && (numberRight != 0);
            case OR -> (numberLeft != 0) || (numberRight != 0);

            default -> numberLeft == numberRight;
        });
    }

    @Override
    public String toString() {
        return String.format("[%s %s %s]", left, operation.getName(), right);
    }
}
