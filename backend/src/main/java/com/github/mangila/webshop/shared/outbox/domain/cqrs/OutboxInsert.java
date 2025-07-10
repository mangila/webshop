package com.github.mangila.webshop.shared.outbox.domain.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxPayload;

import java.util.UUID;

public record OutboxInsert(
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload
) {

    public static OutboxInsert from(Domain domain, Event event, UUID aggregateId, JsonNode payload) {
        return new OutboxInsert(domain, event, new OutboxAggregateId(aggregateId), new OutboxPayload(payload));
    }
}
