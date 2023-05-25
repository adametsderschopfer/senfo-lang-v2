package com.senfo.parser.ast;

public final class ForStatement implements IStatement {
    private final IStatement initialization;
    private final IExpression termination;
    private final IStatement increment;
    private final IStatement statement;

    public ForStatement(IStatement initialization, IExpression termination, IStatement increment, IStatement block) {
        this.initialization = initialization;
        this.termination = termination;
        this.increment = increment;
        this.statement = block;
    }

    @Override
    public void execute() {
        for(initialization.execute(); termination.eval().asDouble() != 0; increment.execute()) {
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                break;
            } catch (ContinueStatement cs) {
                // continue;
            }
        }
    }

    @Override
    public String toString() {
       return "for " + initialization + ", " + termination + ", " + increment + " " + statement;
    }
}
