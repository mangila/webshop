package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.model.Sku;

public record ProductVariant(
        Sku sku,
        ProductAttributes attributes
) {
}
