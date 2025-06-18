package com.github.mangila.webshop.product.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;

public record Product(
        String id,
        String name,
        BigDecimal price,
        Instant created,
        Instant updated,
        JsonNode attributes
) {
    public static final Product EMPTY = new Product(null, null, null, null, null, null);
}
