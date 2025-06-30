package com.github.mangila.webshop.backend.product.domain.command;

import com.github.mangila.webshop.backend.product.domain.util.ProductId;

public record ProductDeleteCommand(
        @ProductId
        String id
) {
}
