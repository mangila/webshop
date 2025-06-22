package com.github.mangila.webshop.backend.common.util.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;

public class CommandException extends ApiException {

    private final Class<?> command;

    public CommandException(@NotNull Class<?> command,
                            @NotNull Class<?> resource,
                            @NotNull HttpStatus httpStatus,
                            String message) {
        super(resource, httpStatus, message);
        this.command = command;
    }

    public Class<?> getCommand() {
        return command;
    }
}
