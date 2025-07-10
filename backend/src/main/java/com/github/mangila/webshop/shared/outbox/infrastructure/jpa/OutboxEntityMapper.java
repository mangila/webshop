package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OutboxEntityMapper {

    private final RegistryService registryService;

    public OutboxEntityMapper(RegistryService registryService) {
        this.registryService = registryService;
    }

    public OutboxEntity toEntity(OutboxInsert command) {
        return OutboxEntity.from(
                command.domain().value(),
                command.event().value(),
                command.aggregateId().value(),
                command.payload().value()
        );
    }

    public Outbox toDomain(OutboxEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to value")
        );
        var domain = Domain.from(entity.getDomain(), registryService);
        var event = Event.from(entity.getEvent(), registryService);
        return Outbox.from(
                entity.getId(),
                domain,
                event,
                entity.getAggregateId(),
                entity.getPayload(),
                entity.isPublished(),
                created
        );
    }
}
