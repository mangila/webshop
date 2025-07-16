package com.github.mangila.webshop.shared.domain.exception;

import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import org.jspecify.annotations.Nullable;

public class CqrsException extends RuntimeException {

    private final CqrsOperation operation;
    private final Class<?> domain;

    public CqrsException(String message,
                         CqrsOperation operation,
                         Class<?> domain,
                         @Nullable Throwable cause) {
        super(message, cause);
        this.operation = operation;
        this.domain = domain;
    }

    public CqrsException(String message, CqrsOperation operation, Class<?> domain) {
        this(message, operation, domain, null);
    }

    public CqrsOperation getOperation() {
        return operation;
    }

    public Class<?> getDomain() {
        return domain;
    }
}
