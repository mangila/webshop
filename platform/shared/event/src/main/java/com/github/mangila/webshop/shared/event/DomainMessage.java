package com.github.mangila.webshop.shared.event;

import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;

import java.util.UUID;

public record DomainMessage(
        long outboxId,
        UUID aggregateId,
        Domain domain,
        Event event
) {
}
