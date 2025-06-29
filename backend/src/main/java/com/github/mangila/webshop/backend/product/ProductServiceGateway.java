package com.github.mangila.webshop.backend.product;

import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.EventServiceGateway;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.product.command.ProductCommandService;
import com.github.mangila.webshop.backend.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.backend.product.model.Product;
import com.github.mangila.webshop.backend.product.model.ProductEventType;
import com.github.mangila.webshop.backend.product.query.ProductQueryService;
import com.github.mangila.webshop.backend.product.query.model.ProductByIdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceGateway.class);

    private final JsonMapper jsonMapper;
    private final EventServiceGateway eventServiceGateway;
    private final ProductCommandService commandService;
    private final ProductQueryService queryService;

    public ProductServiceGateway(JsonMapper jsonMapper,
                                 EventServiceGateway eventServiceGateway,
                                 ProductCommandService commandService,
                                 ProductQueryService queryService) {
        this.jsonMapper = jsonMapper;
        this.eventServiceGateway = eventServiceGateway;
        this.commandService = commandService;
        this.queryService = queryService;
    }

    public Product findById(ProductByIdQuery query) {
        return queryService.findById(query);
    }

    @Transactional
    public Event upsert(ProductUpsertCommand command) {
        Product product = commandService.upsert(command);
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                product.id(),
                ProductEventType.PRODUCT_UPSERTED.name(),
                jsonMapper.toJsonNode(product)
        );
        log.info("{} -- {} -- {}", event.type(), product, event);
        return event;
    }

    @Transactional
    public Event delete(ProductDeleteCommand command) {
        Product product = commandService.delete(command);
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                product.id(),
                ProductEventType.PRODUCT_DELETED.name(),
                jsonMapper.toJsonNode(product)
        );
        log.info("{} -- {} -- {}", event.type(), product, event);
        return event;
    }
}
