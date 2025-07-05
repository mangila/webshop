package com.github.mangila.webshop.backend.product.domain.command;

import com.github.mangila.webshop.backend.product.domain.model.ProductId;

import java.util.UUID;

public record ProductDeleteCommand(ProductId id) {

    public static ProductDeleteCommand from(UUID productId) {
        return new ProductDeleteCommand(ProductId.from(productId));
    }
}
