package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;

import java.util.UUID;

public record OutboxSequence(OutboxAggregateId aggregateId, int value) {
    public static OutboxSequence from(UUID aggregateId, int sequence) {
        OutboxAggregateId outboxAggregateId = new OutboxAggregateId(aggregateId);
        return new OutboxSequence(outboxAggregateId, sequence);
    }

    public static OutboxSequence initial(UUID aggregateId) {
        final int initial = 1;
        return new OutboxSequence(new OutboxAggregateId(aggregateId), initial);
    }

    public static OutboxSequence incrementFrom(OutboxSequence currentSequence) {
        final int increment = currentSequence.value() + 1;
        return new OutboxSequence(currentSequence.aggregateId(), increment);
    }
}
