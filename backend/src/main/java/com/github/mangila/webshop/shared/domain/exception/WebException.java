package com.github.mangila.webshop.shared.domain.exception;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;

public class WebException extends RuntimeException {

    private final HttpStatus httpStatus;

    public WebException(String message,
                        HttpStatus httpStatus,
                        @Nullable Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
