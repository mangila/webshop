package com.github.mangila.webshop.product.infrastructure.event;

import com.github.mangila.webshop.product.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductOutboxMapper {

    public ProductOutboxDto toDto(Product product) {
        return ProductOutboxDto.from(
                product.getId().value(),
                product.getName().value(),
                product.getPrice().value(),
                product.getAttributes(),
                product.getUnit(),
                product.getCreated()
        );
    }
}
