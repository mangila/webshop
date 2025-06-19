package com.github.mangila.webshop.event.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.common.util.JsonMapper;

import java.time.Instant;

public record Event(
        Long id,
        String topic,
        String type,
        String aggregateId,
        JsonNode data,
        Instant created
) {
    public static final Event EMPTY = new Event(null, null, null, null, null, null);

    public static Event from(EventEntity entity, JsonMapper jsonMapper) {
        return new Event(
                entity.id(),
                entity.topic(),
                entity.type(),
                entity.aggregateId(),
                jsonMapper.toJsonNode(entity.data()),
                entity.created().toInstant()
        );
    }

}
