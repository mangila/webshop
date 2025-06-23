package com.github.mangila.webshop.backend.common.util.exception;

public class DatabaseException extends ApiException {

    private final String sql;
    private final Object[] params;

    public DatabaseException(Class<?> resource,
                             String message,
                             String sql,
                             Object[] params,
                             Throwable cause) {
        super(message, resource, cause);
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }
}
