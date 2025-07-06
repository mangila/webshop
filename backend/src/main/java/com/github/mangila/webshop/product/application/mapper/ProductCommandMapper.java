package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductCommandMapper {

    public ProductInsert toDomain(UUID uuid, ProductInsertCommand command) {
        return ProductInsert.from(
                uuid,
                command.name(),
                command.price(),
                command.attributes(),
                command.unit()
        );
    }
}
