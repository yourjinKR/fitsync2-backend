package app.fitsync.global.util;

import java.util.Map;

public final class AttrUtils {
    private AttrUtils() {}

    public static Map<String, Object> asMap(Object value, String keyNameForError) {
        if (value == null) {
            throw new IllegalArgumentException("Missing map attribute: " + keyNameForError);
        }
        if (!(value instanceof Map)) {
            throw new IllegalArgumentException("Attribute is not a map: " + keyNameForError);
        }
        return (Map<String, Object>) value;
    }

    public static String str(Object value) {
        return value == null ? null : String.valueOf(value);
    }

    public static String requiredStr(Object value, String keyNameForError) {
        String s = str(value);
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Missing required attribute: " + keyNameForError);
        }
        return s;
    }
}
