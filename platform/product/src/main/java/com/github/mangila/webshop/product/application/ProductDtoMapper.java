package com.github.mangila.webshop.product.application;

import com.github.mangila.webshop.product.domain.Product;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class ProductDtoMapper {

    public @Valid ProductDto toDto(Product product) {
        return new ProductDto(
                product.id().value(),
                product.name().value(),
                product.attributes().value(),
                product.created().value(),
                product.updated().value()
        );
    }
}
