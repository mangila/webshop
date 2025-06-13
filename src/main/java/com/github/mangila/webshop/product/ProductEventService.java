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
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductEventService {

    private static final Logger log = LoggerFactory.getLogger(ProductEventService.class);

    private final EventService eventService;
    private final EventMapper eventMapper;

    public ProductEventService(EventService eventService,
                               EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    public Product processMutation(ProductEventType intent, Product product) {
        try {
            return switch (intent) {
                case CREATE_NEW -> createNewProductEvent(intent, product);
                case UPDATED -> null;
                case DELETED -> null;
                case PRICE_CHANGED -> null;
                case CATEGORY_CHANGED -> null;
                case DESCRIPTION_UPDATED -> null;
                case IMAGE_UPDATED -> null;
            };
        } catch (Exception e) {
            log.error("ERR", e);
        }
        return null;
    }

    private Product createNewProductEvent(ProductEventType eventType, Product product) {
        // TODO validation
        Event event = null;
        try {
            event = eventMapper.toEvent(
                    ChannelTopic.PRODUCTS,
                    product.getId(),
                    eventType.toString(),
                    product
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        eventService.insertNewEvent(event);
        return product;
    }
}
