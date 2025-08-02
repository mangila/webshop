package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.rest.request.ProductIdRequest;
import com.github.mangila.webshop.product.application.rest.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductRestMapper {

    public ProductInsertCommand toCommand(UUID id, ProductInsertRequest request) {
        return new ProductInsertCommand(
                new ProductId(id),
                new ProductName(request.name()),
                new ProductAttributes(request.attributes())
        );
    }

    public ProductId toDomain(@Valid ProductIdRequest request) {
        return new ProductId(request.value());
    }
}
