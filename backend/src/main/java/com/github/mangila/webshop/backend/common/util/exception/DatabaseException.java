package com.github.mangila.webshop.backend.common.util.exception;

import org.springframework.http.HttpStatus;

public class DatabaseException extends ApiException {

    private final String sql;
    private final Object[] params;
    private final Throwable cause;

    public DatabaseException(Class<?> resource,
                             String message,
                             String sql,
                             Object[] params,
                             Throwable cause) {
        super(message, resource, HttpStatus.INTERNAL_SERVER_ERROR);
        this.sql = sql;
        this.params = params;
        this.cause = cause;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }

    public Throwable getCause() {
        return cause;
    }
}
