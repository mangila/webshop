package com.github.mangila.webshop.product.application.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.ProductUnit;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductInsertCommand(
        @NotNull String name,
        @NotNull BigDecimal price,
        @NotNull ObjectNode attributes,
        @NotNull ProductUnit unit
) {
}
