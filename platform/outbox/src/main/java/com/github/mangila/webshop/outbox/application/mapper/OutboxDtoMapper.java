package com.github.mangila.webshop.outbox.application.mapper;

import com.github.mangila.webshop.outbox.application.OutboxDto;
import com.github.mangila.webshop.outbox.domain.Outbox;
import org.springframework.stereotype.Component;

@Component
public class OutboxDtoMapper {
    public OutboxDto toDto(Outbox outbox) {
        return new OutboxDto(
                outbox.getId().value(),
                outbox.getDomain().value(),
                outbox.getEvent().value(),
                outbox.getAggregateId().value(),
                outbox.getPayload().value(),
                outbox.getPublished().value(),
                outbox.getSequence().value(),
                outbox.getCreated().value()
        );
    }
}
