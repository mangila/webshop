package com.github.mangila.webshop.backend.event.domain.command;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.UUID;

public record EventPublishCommand(
        String topic,
        String eventType,
        UUID aggregateId,
        JsonNode payload
) {
}
