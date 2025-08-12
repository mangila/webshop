package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Utility class for ensuring specific conditions are met at runtime.
 * This class provides a set of static methods to validate method arguments
 * and enforce constraints, throwing an exception when a condition
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

    public static void notNull(Object object) {
        if (object == null) {
            throw new ApplicationException("Object cannot be null");
        }
    }

    public static void notNull(Object object, Class<?> clazz) {
        if (object == null) {
            throw new ApplicationException("%s cannot be null".formatted(clazz.getSimpleName()));
        }
    }

    public static void notNull(Object object, Supplier<RuntimeException> ex) {
        if (object == null) {
            throw ex.get();
        }
    }

    public static void notEmpty(Object[] array, Class<?> clazz) {
        notNull(array, clazz);
        if (array.length == 0) {
            throw new ApplicationException("%s cannot be empty".formatted(clazz.getSimpleName()));
        }
    }

    public static void notBlank(String value, Class<?> clazz) {
        notNull(value, clazz);
        if (value.isBlank()) {
            throw new ApplicationException("%s cannot be blank".formatted(clazz.getSimpleName()));
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new ApplicationException(message);
        }
    }

    public static void min(int min, int target) {
        if (target < min) {
            throw new ApplicationException("Target must be greater than or equal to %d but was %d"
                    .formatted(min, target)
            );
        }
    }

    public static void min(long min, long target) {
        if (target < min) {
            throw new ApplicationException("Target must be greater than or equal to %d but was %d"
                    .formatted(min, target)
            );
        }
    }

    public static void max(int max, int target) {
        if (target > max) {
            throw new ApplicationException("Target must be less than or equal to %d but was %d"
                    .formatted(max, target)
            );
        }
    }

    public static void beforeOrEquals(Instant instant, Instant target) {
        notNull(instant, Instant.class);
        notNull(target, Instant.class);
        if (!instant.isBefore(target) && !instant.equals(target)) {
            throw new ApplicationException("Target must be before or equal to %s but was %s"
                    .formatted(target.toString(), instant.toString())
            );
        }
    }

    public static void equals(Object object, Object another) {
        if (!Objects.equals(object, another)) {
            throw new ApplicationException("Objects are not equal: %s != %s".formatted(object, another));
        }
    }

    public static void notEquals(Enum<?> object, Enum<?> another, String message) {
        if (object == another) {
            throw new ApplicationException(message);
        }
    }
}
