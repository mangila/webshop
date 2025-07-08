package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class OutboxEntityMapper {

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
        return Outbox.from(
                entity.getId(),
                entity.getDomain(),
                entity.getEvent(),
                entity.getAggregateId(),
                entity.getPayload(),
                entity.isPublished(),
                created
        );
    }
}
