package com.tiendadebarrio.e2e.core;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public final class ExcelRow {
    private final Map<String, String> values;

    public ExcelRow(Map<String, String> values) {
        this.values = new LinkedHashMap<>();
        values.forEach((key, value) -> this.values.put(normalize(key), value == null ? "" : value.trim()));
    }

    public String get(String column) {
        String value = values.get(normalize(column));
        return value == null ? "" : value;
    }

    public boolean execute() {
        String raw = get("Ejecutar").toLowerCase(Locale.ROOT);
        return raw.equals("si") || raw.equals("sí") || raw.equals("yes") || raw.equals("true") || raw.equals("1");
    }

    public String getResolved(String column) {
        return Placeholder.resolve(get(column));
    }

    private static String normalize(String column) {
        return column == null ? "" : column.trim().toLowerCase(Locale.ROOT)
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u").replace("ñ", "n");
    }
}
