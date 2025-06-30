package com.github.mangila.webshop.backend.product.domain.query;

import com.github.mangila.webshop.backend.product.domain.util.ProductId;

public record ProductByIdQuery(@ProductId String id) {
}
