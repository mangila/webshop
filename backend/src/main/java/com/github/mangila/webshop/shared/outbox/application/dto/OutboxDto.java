package com.github.mangila.webshop.shared.outbox.application.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.Instant;
import java.util.UUID;

public record OutboxDto(
        long id,
        String domain,
        String event,
        UUID aggregateId,
        ObjectNode payload,
        boolean published,
        Instant created) {

    public static OutboxDto from(long id, String domain, String event, UUID aggregateId, ObjectNode payload, boolean published, Instant created) {
        return new OutboxDto(id, domain, event, aggregateId, payload, published, created);
    }
}
