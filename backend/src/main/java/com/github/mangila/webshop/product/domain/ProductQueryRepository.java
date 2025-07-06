package com.github.mangila.webshop.product.domain;


import java.util.Optional;

public interface ProductQueryRepository {

    Optional<Product> findById(ProductId productId);

    boolean existsById(ProductId productId);
}
