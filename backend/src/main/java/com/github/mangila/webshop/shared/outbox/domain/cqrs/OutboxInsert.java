package com.github.mangila.webshop.shared.outbox.domain.cqrs;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record OutboxInsert(
        String topic,
        String event,
        UUID aggregateId,
        JsonNode payload
) {

    public static OutboxInsert from(String topic, String event, UUID uuid, JsonNode payload) {
        return new OutboxInsert(topic, event, uuid, payload);
    }
}
