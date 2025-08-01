package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record ProductCreated(Instant value) {
    public ProductCreated {
        Ensure.notNull(value, Instant.class);
    }
}
