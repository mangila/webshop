package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.primitive.ProductUnit;
import com.github.mangila.webshop.shared.Ensure;

public record ProductInsertCommand(
        ProductId id,
        ProductName name,
        ProductAttributes attributes,
        ProductUnit unit
) {
    public ProductInsertCommand {
        Ensure.notNull(id, ProductId.class);
        Ensure.notNull(name, ProductName.class);
        Ensure.notNull(attributes, ProductAttributes.class);
        Ensure.notNull(unit, ProductUnit.class);
    }
}

