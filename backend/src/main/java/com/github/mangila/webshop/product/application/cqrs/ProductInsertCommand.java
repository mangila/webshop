package com.github.mangila.webshop.product.application.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.application.validation.ProductName;
import com.github.mangila.webshop.product.application.validation.ProductPrice;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductInsertCommand(
        @ProductName String name,
        @ProductPrice BigDecimal price,
        @NotNull ObjectNode attributes,
        @NotNull ProductUnit unit
) {
}
