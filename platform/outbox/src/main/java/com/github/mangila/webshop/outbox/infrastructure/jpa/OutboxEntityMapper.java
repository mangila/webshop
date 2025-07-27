package com.github.mangila.webshop.outbox.infrastructure.jpa;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
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
        var domain = new Domain(entity.getDomain(), domainRegistry);
        var event = new Event(entity.getEvent(), eventRegistry);
        var aggregateId = new OutboxAggregateId(entity.getAggregateId());
        return new Outbox(
                new OutboxId(entity.getId()),
                domain,
                event,
                aggregateId,
                new OutboxPayload(entity.getPayload()),
                new OutboxSequence(aggregateId, entity.getSequence()),
                new OutboxPublished(entity.isPublished()),
                new OutboxCreated(entity.getCreated())
        );
    }

    public OutboxMessage toDomain(OutboxMessageProjection projection) {
        var domain = new Domain(projection.domain(), domainRegistry);
        var event = new Event(projection.event(), eventRegistry);
        return new OutboxMessage(
                new OutboxId(projection.id()),
                new OutboxAggregateId(projection.aggregateId()),
                domain,
                event
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
