package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;

public interface ProductCommandRepository {

    Product insert(ProductInsertCommand command);

    boolean deleteById(ProductId productId);
}
