package com.github.mangila.webshop.shared.domain.exception;

import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import org.jspecify.annotations.Nullable;

public class CqrsException extends RuntimeException {

    private final CqrsOperation operation;
    private final Class<?> domain;

    public CqrsException(String message,
                         @Nullable Throwable cause,
                         CqrsOperation operation,
                         Class<?> domain) {
        super(message, cause);
        this.operation = operation;
        this.domain = domain;
    }

    public CqrsOperation getOperation() {
        return operation;
    }

    public Class<?> getDomain() {
        return domain;
    }
}
