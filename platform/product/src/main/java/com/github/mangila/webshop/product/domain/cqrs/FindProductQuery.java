package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductId;

public record FindProductQuery(ProductId id) {
}
