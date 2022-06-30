package com.example.demo;

import java.util.HashMap;
import java.util.Map;

public class ContextHolder {

    private static final Map<String, String> context = new HashMap<>();

    public static void put(String key, String value) {
        context.put(key, value);
    }

    public static String getValue(String key) {
        return context.get(key);
    }
}
