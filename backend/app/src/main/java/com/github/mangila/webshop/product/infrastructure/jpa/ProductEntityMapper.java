package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(UUID id, ProductInsert command) {
        return ProductEntity.from(
                id,
                command.name().value(),
                command.price().value(),
                command.attributes(),
                command.unit()
        );
    }

    public Product toDomain(ProductEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to value")
        );
        Instant updated = entity.getUpdated().orElseThrow(
                () -> new ApplicationException("Updated date is required to map from entity to value")
        );

        return Product.from(
                entity.getId(),
                entity.getName(),
                DomainMoney.from(entity.getCurrency(), entity.getPrice()),
                entity.getAttributes(),
                entity.getUnit(),
                created,
                updated
        );
    }

    public ProductEntity toEntity(Product product) {
        return ProductEntity.from(
                product.getId().value(),
                product.getName().value(),
                product.getPrice().value(),
                product.getAttributes(),
                product.getUnit()
        );
    }
}
