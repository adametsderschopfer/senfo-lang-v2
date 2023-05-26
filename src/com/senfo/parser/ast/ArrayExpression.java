package com.senfo.parser.ast;

import com.senfo.lib.*;

import java.util.List;

public final class ArrayExpression implements IExpression {
    private final List<IExpression> elements;

    public ArrayExpression(List<IExpression> arguments) {
        this.elements = arguments;
    }

    @Override
    public IValue eval() {
        final int size = elements.size();
        final ArrayValue array = new ArrayValue(size);

        for (int i = 0; i < size; i++) {
            array.set(i, elements.get(i).eval());
        }

        return array;
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
