package com.github.mangila.webshop.product.application.rest;

import com.github.mangila.webshop.product.application.rest.request.ProductByIdRequest;
import com.github.mangila.webshop.product.application.rest.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductAttributes;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.primitive.ProductUnit;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductRestMapper {

    public ProductInsertCommand toCommand(UUID id, ProductInsertRequest request) {
        return new ProductInsertCommand(
                new ProductId(id),
                new ProductName(request.name()),
                new ProductAttributes(request.attributes()),
                new ProductUnit(request.unit())
        );
    }

    public ProductId toDomain(@Valid ProductByIdRequest request) {
        return new ProductId(request.value());
    }
}
