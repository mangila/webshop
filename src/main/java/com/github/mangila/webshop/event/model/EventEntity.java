package com.github.mangila.webshop.event.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.sql.Timestamp;

public record EventEntity(
        Long id,
        String topic,
        String type,
        String aggregateId,
        String data,
        Timestamp created
) {
    public static final EventEntity EMPTY = new EventEntity(null, null, null, null, null, null);

    public static EventEntity from(EventTopic topic,
                                   String aggregateId,
                                   String eventType,
                                   JsonNode eventData) {
        return new EventEntity(null,
                topic.toString(),
                eventType,
                aggregateId,
                eventData.toString(),
                null);
    }
}
