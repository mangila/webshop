package com.github.mangila.webshop.product.application;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.JsonMapper;
import com.github.mangila.webshop.shared.OutboxEventMapper;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Component;

@Component
public class ProductOutboxEventMapper {

    private final ProductDtoMapper dtoMapper;
    private final OutboxEventMapper outboxEventMapper;
    private final JsonMapper jsonMapper;

    public ProductOutboxEventMapper(ProductDtoMapper dtoMapper,
                                    OutboxEventMapper outboxEventMapper,
                                    JsonMapper jsonMapper) {
        this.dtoMapper = dtoMapper;
        this.outboxEventMapper = outboxEventMapper;
        this.jsonMapper = jsonMapper;
    }

    public OutboxEvent toEvent(Event event, Product product) {
        Ensure.notNull(event, Event.class);
        Ensure.notNull(product, Product.class);
        return dtoMapper.toDto()
                .andThen(dto -> outboxEventMapper.toEvent(
                        new Domain(Product.class),
                        event,
                        product.id().value(),
                        jsonMapper.toObjectNode(dto)
                ))
                .apply(product);
    }
}
