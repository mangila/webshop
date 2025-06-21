package com.github.mangila.webshop.backend.common.util.exception;

public class RequestedResourceNotFoundException extends RuntimeException {
    public RequestedResourceNotFoundException(Class<?> resource, String message) {
        super(String.format("%s -- %s", resource.getSimpleName(), message));
    }
}
