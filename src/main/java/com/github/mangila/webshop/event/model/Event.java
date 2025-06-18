package com.github.mangila.webshop.event.model;

import com.fasterxml.jackson.databind.JsonNode;

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
}
