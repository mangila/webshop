package com.github.mangila.webshop.product.domain;


public interface ProductQueryRepository {

    Product findById(ProductId productId);

    boolean existsById(ProductId productId);
}
