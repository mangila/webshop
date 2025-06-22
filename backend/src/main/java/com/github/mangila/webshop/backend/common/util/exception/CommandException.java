package com.github.mangila.webshop.backend.common.util.exception;

import org.springframework.http.HttpStatus;

public class CommandException extends ApiException {

    private final Class<?> command;

    public CommandException(Class<?> command,
                            Class<?> resource,
                            HttpStatus httpStatus,
                            String message) {
        super(resource, httpStatus, message);
        this.command = command;
    }

    public Class<?> getCommand() {
        return command;
    }
}
