package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.exception.CqrsException;

public interface ProductCommandRepository {

    Product insert(ProductInsert command);

    void deleteByIdOrThrow(ProductId productId) throws CqrsException;
}
