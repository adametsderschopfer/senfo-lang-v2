package com.senfo.lib;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Variables {
    private static final NumberValue ZERO = new NumberValue(0);
    private static Map<String, IValue> variables;
    private static final Stack<Map<String, IValue>> stack;

    static {
        stack = new Stack<>();
        variables = new HashMap<>();

        variables.put("PI", new NumberValue(Math.PI));
        variables.put("E", new NumberValue(Math.E));
        variables.put("GOLDEN_RATIO", new NumberValue(1.618));
    }

    public static void push() {
        stack.push(new HashMap<>(variables));
    }

    public static void pop() {
        variables = stack.pop();
    }

    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static IValue get(String key) {
        if (!isExists(key)) {
            return ZERO;
        }

        return variables.get(key);
    }

    public static void set(String name, IValue value) {
        variables.put(name, value);
    }
}
