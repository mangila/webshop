package com.github.mangila.webshop.product.infrastructure.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductOutboxDto(UUID id, String name, BigDecimal price, JsonNode attributes, ProductUnit unit,
                               Instant created) {

    public static ProductOutboxDto from(UUID id, String name, BigDecimal price, JsonNode attributes, ProductUnit unit, Instant created) {
        return new ProductOutboxDto(id, name, price, attributes, unit, created);
    }

    public JsonNode toJsonNode(JsonMapper jsonMapper) {
        return jsonMapper.toJsonNode(this);
    }
}
