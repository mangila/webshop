package com.github.mangila.webshop.product.domain.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductUnit;

public record ProductInsertCommand(
        ProductId id,
        ProductName name,
        ObjectNode attributes,
        ProductUnit unit
) {
}
