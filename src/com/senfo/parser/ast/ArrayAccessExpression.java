package com.senfo.parser.ast;

import com.senfo.lib.ArrayValue;
import com.senfo.lib.IValue;
import com.senfo.lib.Variables;

import java.util.List;

public class ArrayAccessExpression implements IExpression {
    private final String variable;
    private final List<IExpression> indices;

    public ArrayAccessExpression(String variable, List<IExpression> indices) {
        this.variable = variable;
        this.indices = indices;
    }

    @Override
    public IValue eval() {
        return getArray().get(lastIndex());
    }

    public ArrayValue getArray() {
        ArrayValue array = consumeArray(Variables.get(variable));
        final int last = indices.size() - 1;
        for (int i = 0; i < last; i++) {
            array = consumeArray(array.get(index(i)));
        }
        return array;
    }

    public int lastIndex() {
        return index(indices.size() - 1);
    }

    private int index(int index) {
        return (int) indices.get(index).eval().asNumber();
    }

    private ArrayValue consumeArray(IValue value) {
        if (value instanceof ArrayValue) {
            return (ArrayValue) value;
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    @Override
    public String toString() {
        return variable + indices;
    }
}
