package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.product.domain.types.ProductUnitType;
import com.github.mangila.webshop.shared.Ensure;

public record ProductUnit(ProductUnitType value) {

    public ProductUnit {
        Ensure.notNull(value, ProductUnitType.class);
    }
}
