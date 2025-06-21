package com.github.mangila.webshop.backend.common.util.exception;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DatabaseOperationFailedException extends RuntimeException {
    public DatabaseOperationFailedException(@NotNull String message,
                                            @NotNull Object[] params,
                                            Throwable throwable) {
        super(String.format(message + ": params %s", Arrays.toString(params)), throwable);
    }
}
