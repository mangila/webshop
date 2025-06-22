package com.github.mangila.webshop.backend.event.model;

import java.sql.Timestamp;

public record EventEntity(
        Long id,
        EventTopic topic,
        String type,
        String aggregateId,
        String data,
        Timestamp created
) {
    public static final EventEntity EMPTY = new EventEntity(null, null, null, null, null, null);
}
