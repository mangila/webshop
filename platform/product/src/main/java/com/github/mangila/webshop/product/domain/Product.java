package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.primitive.*;
import com.github.mangila.webshop.shared.util.Ensure;

public record Product(
        ProductId id,
        ProductName name,
        ProductAttributes attributes,
        ProductUnit unit,
        ProductCreated created,
        ProductUpdated updated) {

    public Product {
        Ensure.notNull(id, "ProductId cannot be null");
        Ensure.notNull(name, "ProductName cannot be null");
        Ensure.notNull(attributes, "Attributes cannot be null");
        Ensure.notNull(unit, "ProductUnit cannot be null");
        Ensure.notNull(created, "Created cannot be null");
        Ensure.notNull(updated, "Updated cannot be null");
        Ensure.isBeforeOrEquals(created.value(), updated.value(), "Created must be before updated");
    }
}

