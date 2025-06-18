package com.github.mangila.webshop.product.command;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.event.command.EventCommandService;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.product.ProductMapper;
import com.github.mangila.webshop.product.ProductValidator;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandType;
import com.github.mangila.webshop.product.model.ProductEventType;
import com.github.mangila.webshop.product.model.ProductMutate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandGateway {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandGateway.class);

    private final JsonMapper jsonMapper;
    private final ProductMapper productMapper;
    private final ProductValidator validator;
    private final ProductCommandService productCommandService;
    private final EventCommandService eventCommandService;

    public ProductCommandGateway(JsonMapper jsonMapper,
                                 ProductMapper productMapper,
                                 ProductValidator validator,
                                 ProductCommandService productCommandService,
                                 EventCommandService eventCommandService) {
        this.jsonMapper = jsonMapper;
        this.productMapper = productMapper;
        this.validator = validator;
        this.productCommandService = productCommandService;
        this.eventCommandService = eventCommandService;
    }

    @Transactional
    public Product processCommand(@NotNull ProductCommandType command, @NotNull ProductMutate mutate) {
        log.info("Processing command -- {} -- {}", command, mutate);
        validator.validateByCommand(command, mutate);
        Product product = switch (command) {
            case UPSERT_PRODUCT -> productCommandService.upsert(mutate);
            case DELETE_PRODUCT -> productCommandService.delete(mutate);
            case UPDATE_PRODUCT_PRICE -> productCommandService.updateProductPrice(mutate);
            case null -> throw new IllegalArgumentException("Null command type");
            default -> throw new IllegalArgumentException("Unknown command type: " + command);
        };
        ProductEventType eventType = ProductEventType.from(command);
        Event event = eventCommandService.emit(
                EventTopic.PRODUCT,
                product.getId(),
                eventType.toString(),
                jsonMapper.toJsonNode(product)
        );
        log.info("Processed command -- {} -- {} -- {}", command, product, event);
        return product;
    }
}
