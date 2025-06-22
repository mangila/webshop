package com.github.mangila.webshop.backend.common.util.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {

    private final String message;
    private final Class<?> resource;
    private final HttpStatus httpStatus;

    public ApiException(String message,
                        Class<?> resource,
                        HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.resource = resource;
        this.httpStatus = httpStatus;
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
