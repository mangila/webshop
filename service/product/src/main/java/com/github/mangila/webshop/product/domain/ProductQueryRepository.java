package com.github.mangila.webshop.product.domain;


import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.exception.CqrsException;

public interface ProductQueryRepository {

    Product findByIdOrThrow(ProductId productId) throws CqrsException;

    boolean existsById(ProductId productId);
}
