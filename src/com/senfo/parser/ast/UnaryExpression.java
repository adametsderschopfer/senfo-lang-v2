package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;

public final class UnaryExpression implements IExpression {
    private final IExpression expr;
    private final char operation;

    public UnaryExpression(char operation, IExpression expr) {
        this.expr = expr;
        this.operation = operation;
    }

    @Override
    public IValue eval() {
        return switch (operation) {
            case '-' -> new NumberValue(-expr.eval().asNumber());
            default -> expr.eval();
        };
    }

    @Override
    public String toString() {
        return operation + "" +expr;
    }
}
