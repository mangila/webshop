package com.github.mangila.webshop.outbox.domain.projection;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

public record OutboxProjection(OutboxId id,
                               OutboxAggregateId aggregateId,
                               Domain domain,
                               Event event,
                               OutboxPayload payload,
                               OutboxSequence sequence) {
    public OutboxProjection {
        Ensure.notNull(id, OutboxId.class);
        Ensure.notNull(aggregateId, OutboxAggregateId.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
        Ensure.notNull(payload, OutboxPayload.class);
        Ensure.notNull(sequence, OutboxSequence.class);
    }
}

