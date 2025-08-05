package com.github.mangila.webshop.outbox.infrastructure.jpa;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxEntityProjection;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import org.springframework.stereotype.Component;

@Component
public class OutboxEntityMapper {

    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public OutboxEntityMapper(DomainRegistry domainRegistry, EventRegistry eventRegistry) {
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
    }

    public OutboxEntity toEntity(OutboxInsertCommand command) {
        return OutboxEntity.from(
                command.domain().value(),
                command.event().value(),
                command.aggregateId().value(),
                command.payload().value(),
                command.sequence().value()
        );
    }

    public Outbox toDomain(OutboxEntity entity) {
        var aggregateId = new OutboxAggregateId(entity.getAggregateId());
        return new Outbox(
                new OutboxId(entity.getId()),
                new Domain(entity.getDomain(), domainRegistry),
                new Event(entity.getEvent(), eventRegistry),
                aggregateId,
                new OutboxPayload(entity.getPayload()),
                entity.getStatus(),
                new OutboxSequence(aggregateId, entity.getSequence()),
                new OutboxUpdated(entity.getUpdated()),
                new OutboxCreated(entity.getCreated())
        );
    }

    public OutboxProjection toDomain(OutboxEntityProjection projection) {
        OutboxAggregateId aggregateId = new OutboxAggregateId(projection.aggregateId());
        return new OutboxProjection(
                new OutboxId(projection.id()),
                aggregateId,
                new Domain(projection.domain(), domainRegistry),
                new Event(projection.event(), eventRegistry),
                new OutboxPayload(projection.payload()),
                new OutboxSequence(aggregateId, projection.sequence())
        );
    }

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
