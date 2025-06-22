package com.github.mangila.webshop.backend.common.util.exception;

import org.springframework.http.HttpStatus;

public class QueryException extends ApiException {

    private final Class<?> query;

    public QueryException(Class<?> query,
                          Class<?> resource,
                          HttpStatus httpStatus,
                          String message) {
        super(resource, httpStatus, message);
        this.query = query;
    }

    public Class<?> getQuery() {
        return query;
    }
}
