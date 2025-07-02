package com.github.mangila.webshop.backend.product.domain.command;

import com.github.mangila.webshop.backend.product.domain.model.ProductId;

public record ProductDeleteCommand(ProductId id) {
}
