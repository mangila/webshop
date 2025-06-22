package com.github.mangila.webshop.backend.common.util.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final Class<?> resource;
    private final HttpStatus httpStatus;
    private final String message;

    public ApiException(@NotNull Class<?> resource,
                        @NotNull HttpStatus httpStatus,
                        String message) {
        this.resource = resource;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public Class<?> getResource() {
        return resource;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
