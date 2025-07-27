package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.JsonMapper;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.DomainEvent;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import org.springframework.stereotype.Component;

@Component
public class ProductEventMapper {

    private final ProductDtoMapper dtoMapper;
    private final JsonMapper jsonMapper;
    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public ProductEventMapper(ProductDtoMapper dtoMapper,
                              JsonMapper jsonMapper, DomainRegistry domainRegistry, EventRegistry eventRegistry) {
        this.dtoMapper = dtoMapper;
        this.jsonMapper = jsonMapper;
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
    }

    public DomainEvent toEvent(ProductEvent event, Product product) {
        var dto = dtoMapper.toDto(product);
        return new DomainEvent(
                new Domain(Product.class, domainRegistry),
                new Event(event, eventRegistry),
                product.id().value(),
                jsonMapper.toObjectNode(dto)
        );
    }
}
