package com.github.mangila.webshop.outbox.infrastructure.jpa;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import org.springframework.stereotype.Component;

@Component
public class OutboxEntityMapper {

    private final RegistryService registryService;

    public OutboxEntityMapper(RegistryService registryService) {
        this.registryService = registryService;
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
        var domain = Domain.from(entity.getDomain(), registryService);
        var event = Event.from(entity.getEvent(), registryService);
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
        var domain = Domain.from(projection.domain(), registryService);
        var event = Event.from(projection.event(), registryService);
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
