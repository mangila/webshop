package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;

public record OutboxInsertCommand(
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload,
        OutboxSequence sequence
) {
}
