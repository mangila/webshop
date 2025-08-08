package com.github.mangila.webshop.shared;

import java.util.function.Function;

public interface QueryAction<Q, R> {

    R query(Q query);

    default Function<Q, R> query() {
        return this::query;
    }
}
