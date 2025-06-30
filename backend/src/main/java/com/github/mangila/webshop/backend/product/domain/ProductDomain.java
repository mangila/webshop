package com.github.mangila.webshop.backend.product.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.util.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductDomain(
        String id,
        String name,
        BigDecimal price,
        Instant created,
        Instant updated,
        JsonNode attributes
) {

    public static ProductDomain from(ProductEntity entity, JsonMapper jsonMapper) {
        return new ProductDomain(
                entity.id(),
                entity.name(),
                entity.price(),
                entity.created().toInstant(),
                entity.updated().toInstant(),
                jsonMapper.toJsonNode(entity.attributes())
        );
    }
}
