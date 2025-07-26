package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.util.Ensure;

import java.time.Instant;

public record ProductUpdated(Instant value) {
    public ProductUpdated {
        Ensure.notNull(value, "Product updated must not be null");
    }
}
