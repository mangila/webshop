package com.github.mangila.webshop.product.domain;


import com.github.mangila.webshop.product.domain.cqrs.FindProductByStatusQuery;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ProductQueryRepository {

    Optional<Product> findById(FindProductByIdQuery query);

    default Function<FindProductByIdQuery, Optional<Product>> findById() {
        return this::findById;
    }

    boolean existsById(ProductId productId);

    default Function<ProductId, Boolean> existsById() {
        return this::existsById;
    }

    List<Product> findByStatus(FindProductByStatusQuery query);
}
