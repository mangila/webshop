package com.github.mangila.webshop.shared.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record DomainEvent(
        Domain domain,
        Event event,
        UUID aggregateId,
        ObjectNode payload
) {
    public DomainEvent {
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
        Ensure.notNull(aggregateId, UUID.class);
        Ensure.notNull(payload, ObjectNode.class);
    }
}
