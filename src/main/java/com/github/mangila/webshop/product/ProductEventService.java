package com.github.mangila.webshop.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.event.Event;
import com.github.mangila.webshop.common.event.EventService;
import com.github.mangila.webshop.common.event.EventTopic;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductEventService {

    private static final Logger log = LoggerFactory.getLogger(ProductEventService.class);

    private final EventTopic topic = EventTopic.PRODUCT;

    private final ObjectMapper objectMapper;
    private final EventService eventService;
    private final ProductValidator validator;
    private final ProductCommandService commandService;

    public ProductEventService(ObjectMapper objectMapper,
                               EventService eventService,
                               ProductValidator validator,
                               ProductCommandService commandService) {
        this.objectMapper = objectMapper;
        this.eventService = eventService;
        this.validator = validator;
        this.commandService = commandService;
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

    @Transactional
    public void acknowledgeEvent(long id) throws JsonProcessingException {
        Event event = eventService.acknowledge(id);
        log.info("Processing event: {}", event);
        ProductEventType eventType = ProductEventType.valueOf(event.getEventType());
        Product product = objectMapper.readValue(event.getEventData(), Product.class);
        log.info("EventType -- {} -- Product -- {}", eventType, product);
        switch (eventType) {
            case CREATE_NEW -> commandService.createNewProduct(product);
            case DELETE -> commandService.deleteProductById(product.getId());
        }
    }

    private Event deleteProductEvent(ProductEventType eventType, Product product) throws JsonProcessingException {
        return eventService.emit(
                topic,
                product.getId(),
                eventType.toString(),
                product
        );
    }

    private Event createNewProductEvent(ProductEventType eventType, Product product) throws JsonProcessingException {
        return eventService.emit(
                topic,
                product.getId(),
                eventType.toString(),
                product
        );
    }

    public List<Event> queryPendingEventsByTopic(EventTopic topic) {
        return eventService.queryPendingEventsByTopic(topic);
    }
}
