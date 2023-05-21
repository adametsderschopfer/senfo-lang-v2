package com.senfo.parser.ast;

import com.senfo.lib.IValue;
import com.senfo.lib.NumberValue;
import com.senfo.lib.StringValue;

public final class ValueExpression implements IExpression {
    private final IValue value;

    public ValueExpression(double value) {
        this.value = new NumberValue(value);
    }

    public ValueExpression(String value) {
        this.value = new StringValue(value);
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
