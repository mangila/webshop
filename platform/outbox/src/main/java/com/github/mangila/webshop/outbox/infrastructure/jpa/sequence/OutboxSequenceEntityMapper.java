package com.github.mangila.webshop.outbox.infrastructure.jpa.sequence;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import org.springframework.stereotype.Component;

@Component
public class OutboxSequenceEntityMapper {
    public OutboxSequence toDomain(OutboxSequenceEntity entity) {
        return OutboxSequence.from(
                entity.getAggregateId(),
                entity.getCurrentSequence()
        );
    }

    public OutboxSequenceEntity toEntity(OutboxSequence outboxSequence) {
        return new OutboxSequenceEntity(
                outboxSequence.aggregateId().value(),
                outboxSequence.value()
        );
    }
}
