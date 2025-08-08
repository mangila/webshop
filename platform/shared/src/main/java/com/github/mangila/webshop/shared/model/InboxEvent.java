package com.github.mangila.webshop.shared.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record InboxEvent(
        long outboxId,
        UUID aggregateId,
        Domain domain,
        Event event,
        ObjectNode payload,
        int sequence,
        EventSource source
) {
    public InboxEvent {
        Ensure.min(1, outboxId);
        Ensure.notNull(aggregateId, UUID.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
        Ensure.notNull(payload, ObjectNode.class);
        Ensure.min(1, sequence);
        Ensure.notNull(source, EventSource.class);
    }
}
