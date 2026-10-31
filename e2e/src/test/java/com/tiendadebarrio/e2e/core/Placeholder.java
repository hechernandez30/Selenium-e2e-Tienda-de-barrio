package com.tiendadebarrio.e2e.core;

public final class Placeholder {
    private Placeholder() {}

    public static String resolve(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return raw.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
    }
}
