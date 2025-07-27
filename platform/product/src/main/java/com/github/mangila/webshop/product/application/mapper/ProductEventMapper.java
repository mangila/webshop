package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.model.DomainEvent;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.JsonMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductEventMapper {

    private final ProductDtoMapper dtoMapper;
    private final JsonMapper jsonMapper;
    private final RegistryService registryService;

    public ProductEventMapper(ProductDtoMapper dtoMapper,
                              JsonMapper jsonMapper,
                              RegistryService registryService) {
        this.dtoMapper = dtoMapper;
        this.jsonMapper = jsonMapper;
        this.registryService = registryService;
    }

    public DomainEvent toEvent(ProductEvent event, Product product) {
        var dto = dtoMapper.toDto(product);
        return new DomainEvent(
                new Domain(Product.class, registryService),
                new Event(event, registryService),
                product.id().value(),
                jsonMapper.toObjectNode(dto)
        );
    }
}
