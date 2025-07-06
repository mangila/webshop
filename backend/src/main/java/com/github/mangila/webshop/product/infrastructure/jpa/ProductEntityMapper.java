package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(ProductInsert command) {
        return ProductEntity.from(
                command.id().value(),
                command.name().value(),
                command.price().value(),
                command.attributes(),
                command.unit()
        );
    }

    public Product toDomain(ProductEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to domain")
        );
        Instant updated = entity.getUpdated().orElseThrow(
                () -> new ApplicationException("Updated date is required to map from entity to domain")
        );
        return Product.from(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getAttributes(),
                entity.getUnit(),
                created,
                updated
        );
    }
}
