package com.github.mangila.webshop.backend.common.exception;

import org.springframework.http.HttpStatus;

public class QueryException extends ApiException {

    private final Class<? extends Record> query;

    public QueryException(String message,
                          Class<? extends Record> query,
                          Class<?> resource,
                          HttpStatus httpStatus) {
        super(message, resource, httpStatus);
        this.query = query;
    }

    public Class<?> getQuery() {
        return query;
    }
}
