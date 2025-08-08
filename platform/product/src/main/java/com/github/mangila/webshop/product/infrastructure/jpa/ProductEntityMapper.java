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
        return new ProductEntity(
                command.id().value(),
                command.name().value(),
                command.attributes().value(),
                Instant.now(),
                Instant.now()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                new ProductName(entity.getName()),
                new ProductAttributes(entity.getAttributes()),
                new ProductVariants(List.of()),
                new ProductCreated(entity.getCreated()),
                new ProductUpdated(entity.getUpdated())
        );
    }
}
