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
        Ensure.notNull(domain, "domain cannot be null");
        Ensure.notNull(event, "event cannot be null");
        Ensure.notNull(aggregateId, "aggregateId cannot be null");
        Ensure.notNull(payload, "payload cannot be null");
    }
}
