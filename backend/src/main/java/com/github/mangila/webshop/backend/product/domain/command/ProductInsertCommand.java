package com.github.mangila.webshop.backend.product.domain.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.product.domain.model.ProductName;
import com.github.mangila.webshop.backend.product.domain.model.ProductPrice;
import com.github.mangila.webshop.backend.product.domain.model.ProductUnit;

public record ProductInsertCommand(
        ProductName name,
        ProductPrice price,
        JsonNode attributes,
        ProductUnit unit
) {
}
