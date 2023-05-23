package com.senfo.parser.ast;

public final class WhileStatement implements IStatement {
    private final IExpression condition;
    private final IStatement statement;

    public WhileStatement(IExpression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        while (condition.eval().asDouble() != 0) {
            statement.execute();
        }
    }

    @Override
    public String toString() {
        return "while " + condition + " {" + statement + "}";
    }
}
