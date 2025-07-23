package com.github.mangila.webshop.product.application;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;

import java.time.Instant;
import java.util.UUID;

public record ProductDto(UUID id,
                         String name,
                         ObjectNode attributes,
                         ProductUnit unit,
                         Instant created,
                         Instant updated) {
}
