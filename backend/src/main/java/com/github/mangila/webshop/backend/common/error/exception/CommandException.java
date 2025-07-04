package com.github.mangila.webshop.backend.common.error.exception;

import org.springframework.http.HttpStatus;

public class CommandException extends ApiException {

    private final Class<? extends Record> command;

    public CommandException(Class<? extends Record> command,
                            Class<?> resource,
                            HttpStatus httpStatus,
                            String message) {
        super(message, resource, httpStatus);
        this.command = command;
    }

    public Class<?> getCommand() {
        return command;
    }
}
