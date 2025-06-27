package com.github.mangila.webshop.backend.event.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.EventTypeRegistry;

import java.time.Instant;

public record Event(
        Long id,
        EventTopic topic,
        EventType type,
        String aggregateId,
        JsonNode data,
        Instant created
) {
    public static final Event EMPTY = new Event(null, null, null, null, null, null);

    public static Event from(EventEntity entity,
                             EventTypeRegistry eventTypeRegistry,
                             JsonMapper jsonMapper) {
        EventType eventType = eventTypeRegistry.get(entity.topic(), entity.type());
        JsonNode eventData = jsonMapper.toJsonNode(entity.data());
        return new Event(
                entity.id(),
                entity.topic(),
                eventType,
                entity.aggregateId(),
                eventData,
                entity.created().toInstant()
        );
    }

}
