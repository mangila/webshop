package com.github.mangila.webshop.backend.product.query.model;

import com.github.mangila.webshop.backend.product.util.annotation.ProductId;

public record ProductQueryById(@ProductId String id) {
}
