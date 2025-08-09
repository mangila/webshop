package com.github.mangila.webshop.outbox.domain.cqrs.command;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;

public record IncrementOutboxSequenceCommand(OutboxAggregateId id) {
    public static IncrementOutboxSequenceCommand from(OutboxAggregateId aggregateId) {
        return new IncrementOutboxSequenceCommand(aggregateId);
    }
}
