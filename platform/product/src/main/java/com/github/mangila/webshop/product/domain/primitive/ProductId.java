package com.github.mangila.webshop.product.domain.primitive;


import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Ensure.notNull(value, "Product id must not be null");
    }
}
