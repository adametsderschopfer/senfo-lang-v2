package com.senfo.parser.ast;

public final class DoWhileStatement implements IStatement {
    private final IExpression condition;
    private final IStatement statement;

    public DoWhileStatement(IExpression condition, IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public void execute() {
        do {
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        } while (condition.eval().asDouble() != 0);
    }

    @Override
    public String toString() {
        return "do " + " {" + statement + "}" + " while " + condition;
    }
}
