package com.github.mangila.webshop.outbox.domain.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.Ensure;

public record OutboxMessage(OutboxId id,
                            OutboxAggregateId aggregateId,
                            Domain domain,
                            Event event) {
    public OutboxMessage {
        Ensure.notNull(id, OutboxId.class);
        Ensure.notNull(aggregateId, OutboxAggregateId.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
    }
}

