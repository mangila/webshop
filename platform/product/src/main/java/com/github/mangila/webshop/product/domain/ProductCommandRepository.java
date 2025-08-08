package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;

import java.util.Optional;

public interface ProductCommandRepository {

    Product create(CreateProductCommand command);

    Optional<Product> delete(DeleteProductCommand command);
}
