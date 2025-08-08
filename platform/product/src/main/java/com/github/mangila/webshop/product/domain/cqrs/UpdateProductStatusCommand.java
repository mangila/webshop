package com.github.mangila.webshop.product.domain.cqrs;

import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;

public record UpdateProductStatusCommand(ProductId id, ProductStatusType status) {
}
