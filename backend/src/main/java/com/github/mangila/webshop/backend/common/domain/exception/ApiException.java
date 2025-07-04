package com.github.mangila.webshop.backend.common.domain.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final Class<?> resource;
    private final HttpStatus httpStatus;

    public ApiException(String message,
                        Class<?> resource,
                        HttpStatus httpStatus,
                        @Nullable Throwable cause) {
        super(message, cause);
        this.resource = resource;
        this.httpStatus = httpStatus;
    }

    public ApiException(String message,
                        Class<?> resource,
                        Throwable cause) {
        this(message, resource, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

    public ApiException(String message,
                        Class<?> resource,
                        HttpStatus httpStatus) {
        this(message, resource, httpStatus, null);
    }

    public ApiException(String message, Class<?> resource) {
        this(message, resource, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Class<?> getResource() {
        return resource;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
