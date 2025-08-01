package com.github.mangila.webshop.outbox.infrastructure.jpa.projection;

import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record OutboxProjection(long id,
                               UUID aggregateId,
                               String domain,
                               String event) {
    public OutboxProjection {
        Ensure.min(1, id);
        Ensure.notNull(aggregateId, UUID.class);
        Ensure.notNull(domain, String.class);
        Ensure.notNull(event, String.class);
    }
}

