package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.event.command.EventCommandService;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.product.ProductValidator;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandGateway.class);

    private final JsonMapper jsonMapper;
    private final ProductValidator validator;
    private final ProductCommandService productCommandService;
    private final EventCommandService eventCommandService;

    public ProductCommandGateway(JsonMapper jsonMapper,
                                 ProductValidator validator,
                                 ProductCommandService productCommandService,
                                 EventCommandService eventCommandService) {
        this.jsonMapper = jsonMapper;
        this.validator = validator;
        this.productCommandService = productCommandService;
        this.eventCommandService = eventCommandService;
    }

    @Transactional
    public Product processCommand(@NotNull ProductCommand command) {
        log.info("Processing command -- {}", command);
        var type = command.type();
        validator.validateByCommand(command);
        Product product = switch (type) {
            case UPSERT_PRODUCT -> productCommandService.upsert(command);
            case DELETE_PRODUCT -> productCommandService.delete(command);
            case UPDATE_PRODUCT_PRICE -> productCommandService.updateProductPrice(command);
            case null -> throw new IllegalArgumentException("Null command type");
            default -> throw new IllegalArgumentException("Unknown command type: " + command);
        };
        ProductEventType eventType = ProductEventType.from(type);
        Event event = eventCommandService.emit(
                EventTopic.PRODUCT,
                product.getId(),
                eventType.toString(),
                jsonMapper.toJsonNode(product)
        );
        log.info("Processed command -- {} -- {} -- {}", type, product, event);
        return product;
    }
}
