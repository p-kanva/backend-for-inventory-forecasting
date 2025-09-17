package com.example.inventory.util;

import java.util.UUID;

public class Correlation {
    private static final ThreadLocal<String> CURRENT = ThreadLocal.withInitial(() -> UUID.randomUUID().toString());

    public static String get() {
        return CURRENT.get();
    }

    public static void clear() {
        CURRENT.remove();
    }
}
