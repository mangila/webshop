package com.github.mangila.webshop.shared;

import jakarta.validation.constraints.NotNull;

import java.util.function.Function;

public interface QueryAction<Q, R> {

    R execute(@NotNull Q query);

    default Function<Q, R> execute() {
        return this::execute;
    }
}
