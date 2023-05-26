package com.senfo.parser.ast;

import com.senfo.lib.ArrayValue;
import com.senfo.lib.IValue;
import com.senfo.lib.Variables;

public class ArrayAssignmentStatement implements IStatement {
    private final String variable;
    private final IExpression index;
    private final IExpression expression;

    public ArrayAssignmentStatement(String variable, IExpression index, IExpression expression) {
        this.variable = variable;
        this.index = index;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final IValue var = Variables.get(variable);

        if (var instanceof ArrayValue) {
            final ArrayValue array = (ArrayValue) var;
            array.set((int) index.eval().asNumber(), expression.eval());
        } else {
            throw new RuntimeException("Array expected");
        }
    }

    @Override
    public String toString() {
        return String.format("%s[%s] = %s", variable, index, expression);
    }
}
