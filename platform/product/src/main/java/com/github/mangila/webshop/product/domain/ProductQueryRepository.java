package com.github.mangila.webshop.product.domain;


import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;

import java.util.Optional;
import java.util.function.Function;

public interface ProductQueryRepository {

    Optional<Product> findById(FindProductQuery query);

    default Function<FindProductQuery, Optional<Product>> findById() {
        return this::findById;
    }

    boolean existsById(ProductId productId);

    default Function<ProductId, Boolean> existsById() {
        return this::existsById;
    }
}
