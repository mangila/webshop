package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.web.request.ProductInsertRequest;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductRequestMapper {

    public ProductInsertCommand toCommand(UUID id, ProductInsertRequest request) {
        return new ProductInsertCommand(
                new ProductId(id),
                new ProductName(request.name()),
                request.attributes(),
                request.unit()
        );
    }
}
