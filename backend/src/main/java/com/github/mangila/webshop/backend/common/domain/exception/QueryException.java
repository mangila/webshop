package com.github.mangila.webshop.backend.common.domain.exception;

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

    public QueryException(String message,
                          Class<? extends Record> query,
                          Class<?> resource) {
        this(message, query, resource, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Class<?> getQuery() {
        return query;
    }
}
