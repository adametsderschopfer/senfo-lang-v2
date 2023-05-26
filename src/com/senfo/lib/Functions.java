package com.senfo.lib;

import java.util.HashMap;
import java.util.Map;

public class Functions {
    private static final Map<String, IFunction> functions;

    static {
        functions = new HashMap<>();

        functions.put("sin", args -> {
            if (args.length != 1) {
                throw new RuntimeException("One args expected");
            }

            return new NumberValue(Math.sin(args[0].asNumber()));
        });

        functions.put("cos", args -> {
            if (args.length != 1) {
                throw new RuntimeException("One args expected");
            }

            return new NumberValue(Math.cos(args[0].asNumber()));
        });

        functions.put("createCollection", ArrayValue::new);
    }

    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static IFunction get(String key) {
        if (!isExists(key)) {
            throw new RuntimeException("Unknown function: " + key);
        }

        return functions.get(key);
    }

    public static void set(String name, IFunction value) {
        functions.put(name, value);
    }
}
