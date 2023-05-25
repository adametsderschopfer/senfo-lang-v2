package com.senfo.parser.ast;

public final class ContinueStatement extends RuntimeException implements IStatement {
    @Override
    public void execute() {
        throw this;
    }

    @Override
    public String toString() {
        return "continue";
    }
}

