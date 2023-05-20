package com.senfo.lib;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    private final static Map<String, Double> variables;

    static {
        variables = new HashMap<>();

        variables.put("PI", Math.PI);
        variables.put("E", Math.E);
        variables.put("GOLDEN_RATIO", 1.618);
    }

    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static double get(String key) {
        if (!isExists(key)) {
            return 0;
        }

        return variables.get(key);
    }

    public static void set(String name, double value) {
        variables.put(name, value);
    }
}
