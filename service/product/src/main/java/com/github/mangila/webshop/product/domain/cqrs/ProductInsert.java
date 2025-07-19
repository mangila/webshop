package com.github.mangila.webshop.product.domain.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductUnit;

public record ProductInsert(
        ProductName name,
        ObjectNode attributes,
        ProductUnit unit
) {
    public static ProductInsert from(String name, ObjectNode attributes, ProductUnit unit) {
        return new ProductInsert(new ProductName(name), attributes, unit);
    }
}
