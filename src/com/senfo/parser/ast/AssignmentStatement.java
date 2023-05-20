package com.senfo.parser.ast;

import com.senfo.lib.Variables;

public class AssignmentStatement implements IStatement {
    private final String variable;
    private final IExpression expression;

    public AssignmentStatement(String variable, IExpression expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void execute() {
        final double result = expression.eval();
        Variables.set(variable, result);
    }

    @Override
    public String toString() {
        return String.format("%s = %s", variable, expression);
    }
}
