package com.github.mangila.webshop.product.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.common.util.JsonMapper;

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

    public static Product from(ProductEntity entity, JsonMapper jsonMapper) {
        return new Product(
                entity.id(),
                entity.name(),
                entity.price(),
                entity.created().toInstant(),
                entity.updated().toInstant(),
                jsonMapper.toJsonNode(entity.attributes())
        );
    }
}
