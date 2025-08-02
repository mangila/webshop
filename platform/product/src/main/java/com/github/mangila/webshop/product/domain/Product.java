package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.primitive.*;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.annotation.DomainObject;

@DomainObject
public record Product(
        ProductId id,
        ProductName name,
        ProductAttributes attributes,
        ProductVariants variants,
        ProductCreated created,
        ProductUpdated updated) {

    public Product {
        Ensure.notNull(id, ProductId.class);
        Ensure.notNull(name, ProductName.class);
        Ensure.notNull(attributes, ProductAttributes.class);
        Ensure.notNull(variants, ProductVariants.class);
        Ensure.notNull(created, ProductCreated.class);
        Ensure.notNull(updated, ProductUpdated.class);
        Ensure.beforeOrEquals(created.value(), updated.value());
    }
}

