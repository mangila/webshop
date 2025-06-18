package com.github.mangila.webshop.product.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

public record Product(
    String id,
    String name,
    String description,
    BigDecimal price,
    URI imageUrl,
    String category,
    Instant created,
    Instant updated,
    JsonNode extensions
) {
    public static final Product EMPTY = new Product(null, null, null, null, null, null, null, null, null);
}
