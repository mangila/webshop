package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.primitive.*;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.function.Function;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(CreateProductCommand command) {
        return new ProductEntity(
                command.id().value(),
                command.name().value(),
                command.attributes().value(),
                ProductStatusType.INACTIVE,
                Instant.now(),
                Instant.now()
        );
    }

    public Function<CreateProductCommand, ProductEntity> toEntity() {
        return this::toEntity;
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
}
