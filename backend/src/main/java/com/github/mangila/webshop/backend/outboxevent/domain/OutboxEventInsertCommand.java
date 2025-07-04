package com.github.mangila.webshop.backend.outboxevent.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record OutboxEventInsertCommand(
        String topic,
        String type,
        UUID aggregateId,
        JsonNode payload
) {
    public static OutboxEventInsertCommand from(Enum<?> topic, Enum<?> type, UUID value, JsonNode jsonNode) {
        return new OutboxEventInsertCommand(topic.name(), type.name(), value, jsonNode);
    }
}
