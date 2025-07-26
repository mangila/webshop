package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.primitive.ProductUnit;
import com.github.mangila.webshop.shared.util.Ensure;

public record ProductInsertCommand(
        ProductId id,
        ProductName name,
        ProductAttributes attributes,
        ProductUnit unit
) {
    public ProductInsertCommand {
        Ensure.notNull(id, "ProductId cannot be null");
        Ensure.notNull(name, "ProductName cannot be null");
        Ensure.notNull(attributes, "Attributes cannot be null");
        Ensure.notNull(unit, "ProductUnit cannot be null");
    }
}
