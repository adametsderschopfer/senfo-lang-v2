package com.senfo.parser.ast;

import com.senfo.lib.IValue;

public final class ReturnStatement extends RuntimeException implements IStatement {
    private final IExpression expression;
    private IValue result;

    public ReturnStatement(IExpression expression) {
        this.expression = expression;
    }

    public IValue getResult() {
        return result;
    }

    @Override
    public void execute() {
        result = expression.eval();
        throw this;
    }

    @Override
    public String toString() {
        return "break";
    }
}
