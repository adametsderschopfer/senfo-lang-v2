package com.senfo.parser.ast;

import com.senfo.lib.Functions;
import com.senfo.lib.UserDefineFunction;

import java.util.List;

public final class FunctionDefineStatement implements IStatement {
    private final String name;
    private final List<String> argNames;
    private final IStatement body;

    public FunctionDefineStatement(String name, List<String> argNames, IStatement body) {
        this.name = name;
        this.argNames = argNames;
        this.body = body;
    }

    @Override
    public void execute() {
        Functions.set(name, new UserDefineFunction(argNames, body));
    }

    @Override
    public String toString() {
        return "def (" + argNames.toString() + ") " + body.toString();
    }
}
