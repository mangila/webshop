package com.github.mangila.webshop.product.domain;


import com.github.mangila.webshop.shared.domain.exception.CqrsException;

public interface ProductQueryRepository {

    Product findById(ProductId productId) throws CqrsException;

    boolean existsById(ProductId productId);
}
