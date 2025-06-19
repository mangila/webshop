package com.github.mangila.webshop.product.command.model;

import com.github.mangila.webshop.product.util.ProductId;

public record ProductDeleteCommand(
        @ProductId
        String id
) {
}
