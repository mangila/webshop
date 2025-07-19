package com.github.mangila.webshop.outbox.domain.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

import java.util.UUID;

public record OutboxInsert(
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload
) {

    public static OutboxInsert from(Domain domain, Event event, UUID aggregateId, ObjectNode payload) {
        return new OutboxInsert(domain, event, new OutboxAggregateId(aggregateId), new OutboxPayload(payload));
    }
}
