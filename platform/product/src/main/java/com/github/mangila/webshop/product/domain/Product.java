package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.primitive.*;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.annotation.Domain;

@Domain
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
        Ensure.notNull(attributes, "ProductAttributes cannot be null");
        Ensure.notNull(unit, "ProductUnit cannot be null");
        Ensure.notNull(created, "ProductCreated cannot be null");
        Ensure.notNull(updated, "ProductUpdated cannot be null");
        Ensure.beforeOrEquals(created.value(), updated.value(), "Created must be before or equals updated");
    }
}

