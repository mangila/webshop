package com.github.mangila.webshop.shared.exception;

public class ResourceNotFoundException extends RuntimeException {

    private final Class<?> resource;
    private final Object identifier;

    public ResourceNotFoundException(Class<?> resource, Object identifier) {
        super(String.format("Resource %s with identifier %s not found", resource.getSimpleName(), identifier));
        this.resource = resource;
        this.identifier = identifier;
    }

    public Class<?> resource() {
        return resource;
    }

    public Object identifier() {
        return identifier;
    }
}
