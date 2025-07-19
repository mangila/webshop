package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(UUID id, ProductInsert command) {
        return ProductEntity.from(id, command.name().value(), command.attributes(), command.unit());
    }

    public Product toDomain(ProductEntity entity) {
       return Product.from(
                entity.getId(),
                entity.getName(),
                entity.getAttributes(),
                entity.getUnit(),
                entity.getCreated(),
                entity.getUpdated()
        );
    }
}
