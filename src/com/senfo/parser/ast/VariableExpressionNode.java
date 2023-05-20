package com.senfo.parser.ast;

import com.senfo.lib.Constants;

public final class VariableExpressionNode implements IExpressionNode {
    private final String name;

    public VariableExpressionNode(String name) {
        this.name = name;
    }

    @Override
    public double eval() {
        // todo refactor

        if (!Constants.isExists(name)) {
            throw new RuntimeException("Variable does not exists");
        }

        return Constants.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
