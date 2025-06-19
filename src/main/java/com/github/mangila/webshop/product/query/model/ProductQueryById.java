package com.github.mangila.webshop.product.query.model;

import com.github.mangila.webshop.product.util.ProductId;

public record ProductQueryById(@ProductId String id) {
}
