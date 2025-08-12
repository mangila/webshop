package com.github.mangila.webshop.product.domain;

import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface ProductCommandRepository {

    Product create(CreateProductCommand command);

    default Function<CreateProductCommand, Product> create() {
        return this::create;
    }

    Product delete(Product product);

    default UnaryOperator<Product> delete() {
        return this::delete;
    }

    Product updateStatus(Product product);

    default UnaryOperator<Product> updateStatus() {
        return this::updateStatus;
    }
}
