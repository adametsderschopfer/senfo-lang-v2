package com.senfo.parser.ast;

public final class UnaryExpressionNode implements IExpressionNode {
    private final IExpressionNode expr;
    private final char operation;

    public UnaryExpressionNode(char operation, IExpressionNode expr) {
        this.expr = expr;
        this.operation = operation;
    }

    @Override
    public double eval() {
        return switch (operation) {
            case '-' -> -expr.eval();
            default -> expr.eval();
        };
    }

    @Override
    public String toString() {
        return operation + "" +expr;
    }
}
