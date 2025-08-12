package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.primitive.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(CreateProductCommand command) {
        var now = Instant.now();
        return new ProductEntity(
                command.id().value(),
                command.name().value(),
                command.attributes().value(),
                command.status(),
                now,
                now
        );
    }

    public Product toDomain(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                new ProductName(entity.getName()),
                new ProductAttributes(entity.getAttributes()),
                entity.getStatus(),
                new ProductVariants(List.of()),
                new ProductCreated(entity.getCreated()),
                new ProductUpdated(entity.getUpdated())
        );
    }

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id().value(),
                product.name().value(),
                product.attributes().value(),
                product.status(),
                product.created().value(),
                product.updated().value()
        );
    }
}
