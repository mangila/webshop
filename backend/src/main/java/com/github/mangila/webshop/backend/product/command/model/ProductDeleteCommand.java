package com.github.mangila.webshop.backend.product.command.model;

import com.github.mangila.webshop.backend.product.util.annotation.ProductId;

public record ProductDeleteCommand(
        @ProductId
        String id
) {
}
