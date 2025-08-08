package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.types.ProductStatusType;

public record FindProductByStatusQuery(ProductStatusType status, int limit) {
}
