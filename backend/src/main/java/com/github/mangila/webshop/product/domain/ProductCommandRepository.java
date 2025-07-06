package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;

public interface ProductCommandRepository {
    Product insert(ProductInsert command);

    void deleteById(ProductId productId);
}
