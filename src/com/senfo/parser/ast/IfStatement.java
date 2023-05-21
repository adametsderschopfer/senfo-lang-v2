package com.senfo.parser.ast;

public final class IfStatement implements IStatement {
    private final IExpression expression;
    private final IStatement ifStatement, elseStatement;

    public IfStatement(IExpression expression, IStatement ifStatement, IStatement elseStatement) {
        this.expression = expression;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    @Override
    public void execute() {
        final double result = expression.eval().asDouble();

        if (result != 0) {
            ifStatement.execute();
        } else if (elseStatement != null) {
            elseStatement.execute();
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        result.append("if ").append(expression).append(' ').append(ifStatement);

        if (elseStatement != null) {
            result.append("\nelse ").append(elseStatement);
        }

        return result.toString();
    }
}
