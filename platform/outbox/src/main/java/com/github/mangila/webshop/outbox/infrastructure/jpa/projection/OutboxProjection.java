package com.github.mangila.webshop.outbox.infrastructure.jpa.projection;

import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record OutboxProjection(long id,
                               UUID aggregateId,
                               String domain,
                               String event) {
    public OutboxProjection {
        Ensure.min(1, id, "OutboxId must be greater than 0");
        Ensure.notNull(aggregateId, "OutboxAggregateId must not be null");
        Ensure.notNull(domain, "Domain must not be null");
        Ensure.notNull(event, "Event must not be null");
    }
}
