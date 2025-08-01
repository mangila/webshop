package com.github.mangila.webshop.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final Class<?> resource;

    public ResourceNotFoundException(String message, Class<?> resource) {
        super(message);
        this.resource = resource;
    }

    public Class<?> resource() {
        return resource;
    }
}
