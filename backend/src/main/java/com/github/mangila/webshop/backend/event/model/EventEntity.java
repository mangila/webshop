package com.github.mangila.webshop.backend.event.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Timestamp;

public record EventEntity(
        Long id,
        EventTopic eventTopic,
        String type,
        String aggregateId,
        String data,
        Timestamp created
) {
    public static final EventEntity EMPTY = new EventEntity(null, null, null, null, null, null);

    public static EventEntity from(EventTopic eventTopic,
                                   String aggregateId,
                                   String eventType,
                                   JsonNode eventData) {
        return new EventEntity(null,
                eventTopic,
                eventType,
                aggregateId,
                eventData.toString(),
                null);
    }
}
