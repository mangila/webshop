package com.github.mangila.webshop.backend.product.domain;

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
}
