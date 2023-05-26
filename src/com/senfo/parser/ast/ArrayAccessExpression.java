package com.senfo.parser.ast;

import com.senfo.lib.ArrayValue;
import com.senfo.lib.IValue;
import com.senfo.lib.Variables;

public class ArrayAccessExpression implements IExpression {
    private final String variable;
    private final IExpression index;

    public ArrayAccessExpression(String variable, IExpression index) {
        this.variable = variable;
        this.index = index;
    }

    @Override
    public IValue eval() {
        final IValue var = Variables.get(variable);

        if (var instanceof final ArrayValue array) {
            return array.get((int) index.eval().asNumber());
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    public String toString() {
        return String.format("%s[%s] = ", variable, index);
    }
}
