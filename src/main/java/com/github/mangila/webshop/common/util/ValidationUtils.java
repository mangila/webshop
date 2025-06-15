package com.github.mangila.webshop.common.util;

import com.github.mangila.webshop.common.exception.ValidationException;

public final class ValidationUtils {

    private ValidationUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void ensureNotNull(Object object, String message) {
        if (object == null) {
            throw new ValidationException(message);
        }
    }

}
