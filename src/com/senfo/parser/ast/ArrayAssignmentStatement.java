package com.senfo.parser.ast;

import com.senfo.lib.ArrayValue;
import com.senfo.lib.IValue;
import com.senfo.lib.Variables;

public class ArrayAssignmentStatement implements IStatement {
    private final ArrayAccessExpression array;
    private final IExpression expression;

    public ArrayAssignmentStatement(ArrayAccessExpression array, IExpression expression) {
        this.array = array;
        this.expression = expression;
    }

    @Override
    public void execute() {
        array.getArray().set(array.lastIndex(), expression.eval());
    }

    @Override
    public String toString() {
        return String.format("%s = %s", array, expression);
    }
}
