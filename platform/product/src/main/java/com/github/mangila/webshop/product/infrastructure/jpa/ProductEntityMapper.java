package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(ProductInsertCommand command) {
        return ProductEntity.from(command.id().value(), command.name().value(), command.attributes(), command.unit());
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
