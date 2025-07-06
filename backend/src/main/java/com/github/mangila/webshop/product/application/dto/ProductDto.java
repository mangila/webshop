package com.github.mangila.webshop.product.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.product.domain.ProductUnit;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ProductDto(UUID id, String name, BigDecimal price, JsonNode attributes, ProductUnit unit, Instant created,
                         Instant updated) {

    public static ProductDto from(UUID id, String name, BigDecimal price, JsonNode attributes, ProductUnit unit, Instant created, Instant updated) {
        return new ProductDto(id, name, price, attributes, unit, created, updated);
    }

    public JsonNode toJsonNode(JsonMapper jsonMapper) {
        return jsonMapper.toJsonNode(this);
    }
}
