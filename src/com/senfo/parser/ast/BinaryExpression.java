package com.senfo.parser.ast;

public final class BinaryExpression implements IExpression {
    private final IExpression left, right;
    private final char operation;

    public BinaryExpression(char operation, IExpression left, IExpression right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    @Override
    public double eval() {
        return switch (operation) {
            case '+' -> left.eval() + right.eval();
            case '-' -> left.eval() - right.eval();
            case '/' -> left.eval() / right.eval();
            case '*' -> left.eval() * right.eval();
            default -> left.eval();
        };
    }

    @Override
    public String toString() {
        return String.format("[%s %c %s]", left, operation, right);
    }
}
