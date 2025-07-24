package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;

import java.util.Optional;

public interface ProductCommandRepository {

    Product insert(ProductInsertCommand command);

    Optional<Product> deleteById(ProductId productId);
}
