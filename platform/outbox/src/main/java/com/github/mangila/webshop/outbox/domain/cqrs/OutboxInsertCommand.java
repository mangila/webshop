package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

public record OutboxInsertCommand(
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload,
        OutboxSequence sequence
) {

    public OutboxInsertCommand {
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(event, Event.class);
        Ensure.notNull(aggregateId, OutboxAggregateId.class);
        Ensure.notNull(payload, OutboxPayload.class);
        Ensure.notNull(sequence, OutboxSequence.class);
    }

}
