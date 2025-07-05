package com.github.mangila.webshop.shared.outbox.application.dto;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public record OutboxDto(
        Long id,
        String topic,
        String event,
        UUID aggregateId,
        JsonNode payload,
        boolean published,
        Instant created) {

    public static OutboxDto from(Long id, String topic, String event, UUID aggregateId, JsonNode payload, boolean published, Instant created) {
        return new OutboxDto(id, topic, event, aggregateId, payload, published, created);
    }
}

