package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.shared.Ensure;

public record CreateProductCommand(
        ProductId id,
        ProductName name,
        ProductAttributes attributes,
        ProductStatusType status
) {
    public CreateProductCommand {
        Ensure.notNull(id, ProductId.class);
        Ensure.notNull(name, ProductName.class);
        Ensure.notNull(attributes, ProductAttributes.class);
    }
}

