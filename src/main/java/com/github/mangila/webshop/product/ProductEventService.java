package com.github.mangila.webshop.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.common.EventMapper;
import com.github.mangila.webshop.common.EventService;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.common.model.Event;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductEventService {

    private static final Logger log = LoggerFactory.getLogger(ProductEventService.class);

    private final EventService eventService;
    private final EventMapper eventMapper;
    private final ProductValidator validator;

    public ProductEventService(EventService eventService,
                               EventMapper eventMapper,
                               ProductValidator validator) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
        this.validator = validator;
    }

    public Event processMutation(ProductEventType eventType, Product product) throws JsonProcessingException {
        return switch (eventType) {
            case CREATE_NEW -> createNewProductEvent(eventType, product);
            case DELETE -> deleteProductEvent(eventType, product);
            case PRICE_CHANGED -> null;
            case QUANTITY_CHANGED -> null;
            case EXTENSION_CHANGED -> null;
            case null -> throw new IllegalArgumentException(String.format("Invalid eventType: %s", eventType));
        };
    }

    private Event deleteProductEvent(ProductEventType eventType, Product product) throws JsonProcessingException {
        validator.ensureProductId(product);
        var event = eventMapper.toEvent(
                ChannelTopic.PRODUCTS,
                product.getId(),
                eventType.toString(),
                product
        );
        eventService.emit(event);
        return event;
    }

    private Event createNewProductEvent(ProductEventType eventType, Product product) throws JsonProcessingException {
        validator.ensureRequiredFields(product);
        var event = eventMapper.toEvent(
                ChannelTopic.PRODUCTS,
                product.getId(),
                eventType.toString(),
                product
        );
        eventService.emit(event);
        return event;
    }
}
