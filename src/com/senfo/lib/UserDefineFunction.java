package com.senfo.lib;

import com.senfo.parser.ast.IStatement;
import com.senfo.parser.ast.ReturnStatement;

import java.util.List;

public final class UserDefineFunction implements IFunction {
    private final List<String> argNames;
    private final IStatement body;

    public UserDefineFunction(List<String> argNames, IStatement body) {
        this.argNames = argNames;
        this.body = body;
    }

    public int getArgsCount() {
        return argNames.size();
    }

    public String getArgsName(int index) {
        if (index < 0 || index >= getArgsCount()) {
            return "";
        }

        return argNames.get(index);
    }

    @Override
    public IValue execute(IValue... args) {
        try {
            body.execute();
            return new NumberValue(0);
        } catch (ReturnStatement rs) {
            return rs.getResult();
        }
    }
}
