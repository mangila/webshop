package com.github.mangila.webshop.product.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.event.EventServiceGateway;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventCommand;
import com.github.mangila.webshop.event.model.EventCommandType;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductCommandGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandGateway.class);

    private final JsonMapper jsonMapper;
    private final ProductCommandService productCommandService;
    private final EventServiceGateway eventServiceGateway;

    public ProductCommandGateway(JsonMapper jsonMapper,
                                 ProductCommandService productCommandService,
                                 EventServiceGateway eventServiceGateway) {
        this.jsonMapper = jsonMapper;
        this.productCommandService = productCommandService;
        this.eventServiceGateway = eventServiceGateway;
    }

    public Product processCommand(ProductCommand command) {
        log.info("Processing command -- {}", command);
        var type = command.type();
        Product product = switch (type) {
            case UPSERT_PRODUCT -> productCommandService.upsert(command);
            case DELETE_PRODUCT -> productCommandService.delete(command);
            case UPDATE_PRODUCT_PRICE -> productCommandService.updateProductPrice(command);
            case null -> throw new IllegalArgumentException("Null command type");
            default -> throw new IllegalArgumentException("Unknown command type: " + command);
        };
        ProductEventType eventType = ProductEventType.from(type);
        JsonNode eventData = jsonMapper.toJsonNode(product);
        Event event = eventServiceGateway.processCommand(
                new EventCommand(
                        EventCommandType.EMIT_EVENT,
                        EventTopic.PRODUCT.toString(),
                        eventType.toString(),
                        product.id(),
                        eventData.toString()
                )
        );
        log.info("Processed command -- {} -- {} -- {}", type, product, event);
        return product;
    }
}
