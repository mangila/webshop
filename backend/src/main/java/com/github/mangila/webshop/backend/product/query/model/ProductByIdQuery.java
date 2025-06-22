package com.github.mangila.webshop.backend.product.query.model;

import com.github.mangila.webshop.backend.product.util.annotation.ProductId;

public record ProductByIdQuery(@ProductId String id) {
}
