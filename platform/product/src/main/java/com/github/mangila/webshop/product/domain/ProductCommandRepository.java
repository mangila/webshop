package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;

import java.util.Optional;
import java.util.function.Function;

public interface ProductCommandRepository {

    Product create(CreateProductCommand command);

    default Function<CreateProductCommand, Product> create() {
        return this::create;
    }

    Optional<Product> delete(DeleteProductCommand command);

    default Function<DeleteProductCommand, Optional<Product>> delete() {
        return this::delete;
    }
}
