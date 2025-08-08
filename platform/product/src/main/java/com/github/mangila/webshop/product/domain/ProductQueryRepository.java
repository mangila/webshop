package com.github.mangila.webshop.product.domain;


import com.github.mangila.webshop.product.domain.cqrs.FindProductQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;

import java.util.Optional;

public interface ProductQueryRepository {

    Optional<Product> findById(FindProductQuery query);

    boolean existsById(ProductId productId);
}
