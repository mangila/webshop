package com.github.mangila.webshop.product.application;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.JsonMapper;
import com.github.mangila.webshop.shared.event.DomainEvent;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final JsonMapper jsonMapper;

    public ProductMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public DomainEvent toEvent(ProductEvent event, Product product) {
        var dto = toDto(product);
        return new DomainEvent(
                Domain.from(Product.class),
                Event.from(event),
                product.getId().value(),
                jsonMapper.toObjectNode(dto)
        );
    }

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
