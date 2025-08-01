package com.github.mangila.webshop.outbox.application;

import com.github.mangila.webshop.outbox.domain.Outbox;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
public class OutboxDtoMapper {
    public @Valid OutboxDto toDto(Outbox outbox) {
        return new OutboxDto(
                outbox.id().value(),
                outbox.domain().value(),
                outbox.event().value(),
                outbox.aggregateId().value(),
                outbox.payload().value(),
                outbox.status(),
                outbox.sequence().value(),
                outbox.updated().value(),
                outbox.created().value()
        );
    }
}
