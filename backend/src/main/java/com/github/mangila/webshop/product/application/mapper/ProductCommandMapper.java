package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.cqrs.ProductIdCommand;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import org.springframework.stereotype.Component;

@Component
public class ProductCommandMapper {

    public ProductInsert toDomain(ProductInsertCommand command) {
        return ProductInsert.from(
                command.name(),
                command.price(),
                command.attributes(),
                command.unit()
        );
    }

    public ProductId toDomain(ProductIdCommand id) {
        return new ProductId(id.value());
    }
}
