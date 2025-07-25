package com.github.mangila.webshop.shared.util;

import java.util.Objects;

/**
 * Utility class for ensuring specific conditions are met at runtime.
 * This class provides a set of static methods to validate method arguments
 * and enforce constraints, throwing an {@code ApplicationException} when a condition
 * is not satisfied.
 * <p>
 * All methods in this class are intended to be used as precondition checks,
 * ensuring invariants or expected states before or during execution.
 * <p>
 */
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

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ApplicationException(message);
        }
    }
}
