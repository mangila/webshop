package com.github.mangila.webshop.shared.event;

import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import com.github.mangila.webshop.shared.util.Ensure;

import java.util.UUID;

public record DomainMessage(
        long outboxId,
        UUID aggregateId,
        Domain domain,
        Event event
) {
    public DomainMessage {
        Ensure.notNull(aggregateId, "aggregateId cannot be null");
        Ensure.notNull(domain, "domain cannot be null");
        Ensure.notNull(event, "event cannot be null");
    }
}
