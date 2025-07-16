package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.cqrs.ProductIdQuery;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import org.springframework.stereotype.Component;

@Component
public class ProductQueryMapper {

    public ProductId toDomain(ProductIdQuery query) {
        return new ProductId(query.value());
    }
}
