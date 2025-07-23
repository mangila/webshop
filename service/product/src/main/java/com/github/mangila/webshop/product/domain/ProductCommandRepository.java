package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.exception.CqrsException;

public interface ProductCommandRepository {

    Product insert(ProductInsertCommand command);

    void deleteByIdOrThrow(ProductId productId) throws CqrsException;
}
