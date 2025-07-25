package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;

import java.util.UUID;

public record OutboxSequence(OutboxAggregateId aggregateId, int value) {

    public static OutboxSequence from(UUID aggregateId, int sequence) {
        return new OutboxSequence(new OutboxAggregateId(aggregateId), sequence);
    }

    public static OutboxSequence initial(UUID aggregateId) {
        return new OutboxSequence(new OutboxAggregateId(aggregateId), 1);
    }

    public static OutboxSequence incrementFrom(OutboxSequence currentSequence) {
        return new OutboxSequence(currentSequence.aggregateId(), currentSequence.value() + 1);
    }
}
