package com.github.mangila.webshop.shared.model;

import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record DomainMessage(
        long outboxId,
        UUID aggregateId,
        Domain domain,
        Event event
) {
    public DomainMessage {
        Ensure.min(1, outboxId);
        Ensure.notNull(aggregateId, UUID.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
    }
}
