package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductId;

public record DeleteProductCommand(ProductId id) {
}
