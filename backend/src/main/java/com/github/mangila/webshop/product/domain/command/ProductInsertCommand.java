package com.github.mangila.webshop.product.domain.command;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.model.ProductName;
import com.github.mangila.webshop.product.domain.model.ProductPrice;
import com.github.mangila.webshop.product.domain.model.ProductUnit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ProductInsertCommand(
        @Valid @NotNull ProductName name,
        @Valid @NotNull ProductPrice price,
        @Valid @NotNull ObjectNode attributes,
        @NotNull ProductUnit unit
) {
}
