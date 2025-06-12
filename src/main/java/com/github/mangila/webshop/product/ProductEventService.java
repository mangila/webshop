package com.github.mangila.webshop.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.EventService;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.product.model.event.CreateNewProductEvent;
import com.github.mangila.webshop.product.model.event.ProductEventType;
import org.springframework.stereotype.Service;

@Service
public class ProductEventService {

    private static final ChannelTopic CHANNEL_TOPIC = ChannelTopic.PRODUCTS;

    private final EventService eventService;
    private final ObjectMapper objectMapper;

    public ProductEventService(EventService eventService,
                               ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    public void createProduct(CreateNewProductEvent event) throws JsonProcessingException {
        var pgEvent = event.toEvent(
                CHANNEL_TOPIC,
                ProductEventType.CREATED,
                objectMapper);
        eventService.insertEvent(pgEvent);
    }

}
