package com.github.mangila.webshop.shared.util;

import java.util.Objects;

public final class Ensure {

    private Ensure() {
        throw new ApplicationException("Utility class");
    }

    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) {
            throw new ApplicationException(message);
        }
    }

    public static void notEmpty(byte[] bytes, String message) {
        if (Objects.isNull(bytes) || bytes.length == 0) {
            throw new ApplicationException(message);
        }
    }

    public static void notBlank(String value, String message) {
        if (Objects.isNull(value) || value.isBlank()) {
            throw new ApplicationException(message);
        }
    }
}
