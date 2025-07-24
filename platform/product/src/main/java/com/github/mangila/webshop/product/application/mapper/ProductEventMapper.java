package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.event.DomainEvent;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import com.github.mangila.webshop.shared.util.JsonMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductEventMapper {

    private final ProductDtoMapper dtoMapper;
    private final JsonMapper jsonMapper;

    public ProductEventMapper(ProductDtoMapper dtoMapper,
                              JsonMapper jsonMapper) {
        this.dtoMapper = dtoMapper;
        this.jsonMapper = jsonMapper;
    }

    public DomainEvent toEvent(ProductEvent event, Product product) {
        var dto = dtoMapper.toDto(product);
        return new DomainEvent(
                Domain.from(Product.class),
                Event.from(event),
                product.getId().value(),
                jsonMapper.toObjectNode(dto)
        );
    }
}
