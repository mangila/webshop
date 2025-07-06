package com.github.mangila.webshop.product.domain.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.ProductName;
import com.github.mangila.webshop.product.domain.ProductPrice;
import com.github.mangila.webshop.product.domain.ProductUnit;

import java.math.BigDecimal;

public record ProductInsert(
        ProductName name,
        ProductPrice price,
        ObjectNode attributes,
        ProductUnit unit
) {
    public static ProductInsert from(String name, BigDecimal price, ObjectNode attributes, ProductUnit unit) {
        return new ProductInsert(new ProductName(name), new ProductPrice(price), attributes, unit);
    }
}
