package com.github.mangila.webshop.outbox.infrastructure.jpa.projection;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record OutboxEntityProjection(long id,
                                     UUID aggregateId,
                                     String domain,
                                     String event,
                                     ObjectNode payload,
                                     int sequence) {
    public OutboxEntityProjection {
        Ensure.min(1, id);
        Ensure.notNull(aggregateId, UUID.class);
        Ensure.notNull(domain, String.class);
        Ensure.notNull(event, String.class);
        Ensure.notNull(payload, ObjectNode.class);
        Ensure.min(1, sequence);
    }
}

