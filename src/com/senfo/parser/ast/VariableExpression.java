package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.Variables;

public final class VariableExpression implements IExpression {
    private final String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    @Override
    public IValue eval() {
        if (!Variables.isExists(name)) {
            throw new RuntimeException("Variable does not exists");
        }

        return Variables.get(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
