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
        Ensure.min(1, outboxId, "outboxId must be greater than 0");
        Ensure.notNull(aggregateId, "aggregateId cannot be null");
        Ensure.notNull(domain, "domain cannot be null");
        Ensure.notNull(event, "event cannot be null");
    }
}
