package com.github.mangila.webshop.product.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.common.model.Event;

import java.math.BigDecimal;

/**
 * Event record for creating a new product.
 */
public record CreateNewProductEvent(
        String productId,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String category
) {

    public Event toEvent(ChannelTopic topic,
                         ProductEventType eventType,
                         ObjectMapper objectMapper) throws JsonProcessingException {
        var pgEvent = new Event();
        pgEvent.setAggregateId(productId);
        pgEvent.setTopic(topic.toString());
        pgEvent.setEventType(eventType.toString());
        pgEvent.setEventData(objectMapper.writeValueAsString(this));
        return pgEvent;
    }

}