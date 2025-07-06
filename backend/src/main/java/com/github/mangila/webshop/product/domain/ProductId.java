package com.github.mangila.webshop.product.domain;

import java.util.UUID;

public record ProductId(UUID value) {

    public static ProductId from(UUID productId) {
        return new ProductId(productId);
    }
}
