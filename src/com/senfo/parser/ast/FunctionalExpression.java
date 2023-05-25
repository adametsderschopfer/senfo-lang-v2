package com.senfo.parser.ast;

import com.senfo.lib.*;

import java.util.ArrayList;
import java.util.List;

public final class FunctionalExpression implements IExpression {
    private final String name;
    private final List<IExpression> arguments;

    public FunctionalExpression(String name) {
        this.name = name;
        arguments = new ArrayList<>();
    }

    public FunctionalExpression(String name, List<IExpression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public void addArgument(IExpression arg) {
        arguments.add(arg);
    }

    @Override
    public IValue eval() {
        final int size = arguments.size();
        final IValue[] values = new IValue[size];

        for (int i = 0; i < size; i++) {
            values[i] = arguments.get(i).eval();
        }

        final IFunction function = Functions.get(name);

        if (function instanceof UserDefineFunction) {
            final UserDefineFunction userFunction = (UserDefineFunction) function;

            if (size != userFunction.getArgsCount()) {
                throw new RuntimeException("Args count miss match");
            }

            Variables.push();
            for (int i = 0; i < size; i++) {
                Variables.set(userFunction.getArgsName(i), values[i]);
            }

            final IValue result = userFunction.execute(values);
            Variables.pop();

            return result;
        }

        return function.execute(values);
    }

    @Override
    public String toString() {
        return "FunctionalExpression{}";
    }
}
