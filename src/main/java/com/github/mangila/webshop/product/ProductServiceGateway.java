package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.event.EventServiceGateway;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.product.command.ProductCommandService;
import com.github.mangila.webshop.product.command.model.ProductDeleteCommand;
import com.github.mangila.webshop.product.command.model.ProductUpsertCommand;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import com.github.mangila.webshop.product.query.ProductQueryService;
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

    public Product queryById(String id) {
        return queryService.queryById(id);
    }

    @Transactional
    public Product upsert(ProductUpsertCommand command) {
        Product product = commandService.upsert(command);
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                product.id(),
                ProductEventType.PRODUCT_UPSERTED.toString(),
                jsonMapper.toJsonNode(product)
        );
        log.info("{} -- {} -- {}", event.type(), product, event);
        return product;
    }

    @Transactional
    public Product delete(ProductDeleteCommand command) {
        Product product = commandService.delete(command);
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                product.id(),
                ProductEventType.PRODUCT_DELETED.toString(),
                jsonMapper.toJsonNode(product)
        );
        log.info("{} -- {} -- {}", event.type(), product, event);
        return product;
    }
}
