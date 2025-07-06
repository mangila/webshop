package com.github.mangila.webshop.shared.outbox.application.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public record OutboxInsertCommand(
        String topic,
        String event,
        UUID aggregateId,
        ObjectNode payload
) {
    public static OutboxInsertCommand from(Enum<?> topic, Enum<?> event, UUID value, JsonNode jsonNode) {
        return new OutboxInsertCommand(topic.name(), event.name(), value, (ObjectNode) jsonNode);
    }
}
