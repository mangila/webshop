package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.Ensure;

public record OutboxInsertCommand(
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload,
        OutboxSequence sequence
) {

    public OutboxInsertCommand {
        Ensure.notNull(domain, "Domain must not be null");
        Ensure.notNull(event, "Event must not be null");
        Ensure.notNull(aggregateId, "AggregateId must not be null");
        Ensure.notNull(payload, "Payload must not be null");
        Ensure.notNull(sequence, "OutboxSequence must not be null");
    }

}
