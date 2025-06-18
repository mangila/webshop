package com.github.mangila.webshop.product.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record ProductEntity(
        String id,
        String name,
        BigDecimal price,
        Timestamp created,
        Timestamp updated,
        String attributes
) {
    public static final ProductEntity EMPTY = new ProductEntity(null, null, null, null, null, null);
}
