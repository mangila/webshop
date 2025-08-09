package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;

import java.util.Optional;

public class JavaOptionalUtil {

    private JavaOptionalUtil() {
        throw new ApplicationException("Utility class");
    }

    public static <T> T orElseThrowResourceNotFound(Optional<T> optional, Class<?> resource, Object identifier) {
        return optional.orElseThrow(() -> new ResourceNotFoundException(resource, identifier));
    }
}
