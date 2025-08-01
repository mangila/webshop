package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record ProductUpdated(Instant value) {
    public ProductUpdated {
        Ensure.notNull(value, Instant.class);
    }
}
