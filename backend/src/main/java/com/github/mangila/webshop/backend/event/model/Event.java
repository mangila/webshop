package com.github.mangila.webshop.backend.event.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.util.JsonMapper;

import java.time.Instant;

public record Event(
        Long id,
        EventTopic topic,
        String type,
        String aggregateId,
        JsonNode data,
        Instant created
) {
    public static final Event EMPTY = new Event(null, null, null, null, null, null);

    public static Event from(EventEntity entity,
                             JsonMapper jsonMapper) {
        JsonNode eventData = jsonMapper.toJsonNode(entity.data());
        return new Event(
                entity.id(),
                entity.topic(),
                entity.type(),
                entity.aggregateId(),
                eventData,
                entity.created().toInstant()
        );
    }

}
