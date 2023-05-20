package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;

public final class NumberExpression implements IExpression {
    private final IValue value;

    public NumberExpression(double value) {
        this.value = new NumberValue(value);
    }

    @Override
    public IValue eval() {
        return value;
    }

    @Override
    public String toString() {
        return value.asString();
    }
}
