package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.*;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(ProductInsertCommand command) {
        return new ProductEntity(
                command.id().value(),
                command.name().value(),
                command.attributes().value(),
                command.unit().value()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                new ProductName(entity.getName()),
                new ProductAttributes(entity.getAttributes()),
                new ProductUnit(entity.getUnit()),
                new ProductCreated(entity.getCreated()),
                new ProductUpdated(entity.getUpdated())
        );
    }
}
