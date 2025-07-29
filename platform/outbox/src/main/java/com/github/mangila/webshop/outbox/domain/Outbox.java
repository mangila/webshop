
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
        Ensure.notNull(id, "OutboxId must not be null");
        Ensure.notNull(domain, "Domain must not be null");
        Ensure.notNull(event, "Event must not be null");
        Ensure.notNull(aggregateId, "OutboxAggregateId must not be null");
        Ensure.notNull(payload, "OutboxPayload must not be null");
        Ensure.notNull(status, "OutboxStatusType must not be null");
        Ensure.notNull(sequence, "OutboxSequence must not be null");
        Ensure.notNull(updated, "OutboxUpdated must not be null");
        Ensure.notNull(created, "OutboxCreated must not be null");
        Ensure.isBeforeOrEquals(created.value(), updated.value(), "Created must be before or equals updated");
    }
}