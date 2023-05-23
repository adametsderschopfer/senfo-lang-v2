package com.senfo.parser.ast;

import java.util.ArrayList;
import java.util.List;

public class BlockStatement implements IStatement {
    private List<IStatement> statements;

    public BlockStatement() {
        this.statements = new ArrayList<>();
    }

    public void add(IStatement statement) {
        statements.add(statement);
    }

    @Override
    public void execute() {
        for (IStatement statement : statements) {
            statement.execute();
        }
    }

    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder();

        for (IStatement statement : statements) {
            result.append(statement.toString()).append(System.lineSeparator());
        }

        return result.toString();
    }
}
