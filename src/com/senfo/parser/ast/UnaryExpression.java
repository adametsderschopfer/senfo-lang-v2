package com.senfo.parser.ast;

public final class UnaryExpression implements IExpression {
    private final IExpression expr;
    private final char operation;

    public UnaryExpression(char operation, IExpression expr) {
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
