package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;

import java.util.Optional;

/**
 * Utility class providing methods to operate on Optional instances.
 * This class is designed to simplify the handling of Optional values
 * with domain-specific exception throwing behavior.
 * <p>
 * Methods in this class are intended to simplify the management of
 * scenarios where an Optional must contain a value, providing a way
 * to handle absent values with meaningful exceptions.
 */
public final class JavaOptionalUtil {

    private JavaOptionalUtil() {
        throw new ApplicationException("Utility class");
    }

    /**
     * Returns the value of the Optional or throws a ResourceNotFoundException if the Optional is empty.
     */
    public static <T> T orElseThrowResourceNotFound(Optional<T> optional, Class<?> resource, Object identifier) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(resource, identifier));
    }
}
