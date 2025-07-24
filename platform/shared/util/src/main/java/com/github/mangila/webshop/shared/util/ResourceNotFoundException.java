package com.github.mangila.webshop.shared.util;

public class ResourceNotFoundException extends RuntimeException {

    private final Class<?> domain;

    public ResourceNotFoundException(String message, Class<?> domain) {
        super(message);
        this.domain = domain;
    }

    public Class<?> getDomain() {
        return domain;
    }
}
