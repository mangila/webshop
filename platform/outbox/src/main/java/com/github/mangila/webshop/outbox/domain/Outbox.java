
package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

public record Outbox(
        OutboxId id,
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload,
        OutboxStatusType status,
        OutboxSequence sequence,
        OutboxUpdated updated,
        OutboxCreated created) {
    public Outbox {
        Ensure.notNull(id, OutboxId.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
        Ensure.notNull(aggregateId, OutboxAggregateId.class);
        Ensure.notNull(payload, OutboxPayload.class);
        Ensure.notNull(status, OutboxStatusType.class);
        Ensure.notNull(sequence, OutboxSequence.class);
        Ensure.notNull(updated, OutboxUpdated.class);
        Ensure.notNull(created, OutboxCreated.class);
        Ensure.beforeOrEquals(created.value(), updated.value());
    }

    public boolean notPublished() {
        return status != OutboxStatusType.PUBLISHED;
    }
}
