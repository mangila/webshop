package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.ProductDto;
import com.github.mangila.webshop.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId().value(),
                product.getName().value(),
                product.getAttributes(),
                product.getUnit(),
                product.getCreated(),
                product.getUpdated()
        );
    }
}
