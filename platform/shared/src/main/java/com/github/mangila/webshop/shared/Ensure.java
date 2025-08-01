package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;

import java.time.Instant;

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
        if (object == null) {
            throw new ApplicationException(message);
        }
    }

    public static void notEmpty(byte[] array, String message) {
        notNull(array, message);
        if (array.length == 0) {
            throw new ApplicationException(message);
        }
    }

    public static void notEmpty(Object[] array, String message) {
        notNull(array, message);
        if (array.length == 0) {
            throw new ApplicationException(message);
        }
    }

    public static void notBlank(String value, String message) {
        notNull(value, message);
        if (value.isBlank()) {
            throw new ApplicationException(message);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ApplicationException(message);
        }
    }

    public static void min(int min, int target, String message) {
        if (target < min) {
            throw new ApplicationException(message);
        }
    }

    public static void min(long min, long target, String message) {
        if (target < min) {
            throw new ApplicationException(message);
        }
    }

    public static void max(int max, int target, String message) {
        if (target > max) {
            throw new ApplicationException(message);
        }
    }

    public static void beforeOrEquals(Instant instant, Instant target, String message) {
        notNull(instant, message);
        notNull(target, message);
        if (!instant.isBefore(target) && !instant.equals(target)) {
            throw new ApplicationException(message);
        }
    }
}
