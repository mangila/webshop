package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxMessageDto;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

@Component
public class OutboxDtoMapper {

    private final RegistryService registry;

    public OutboxDtoMapper(RegistryService registry) {
        this.registry = registry;
    }

    public OutboxDto toDto(Outbox domain) {
        return OutboxDto.from(
                domain.getId().value(),
                domain.getDomain().value(),
                domain.getEvent().value(),
                domain.getAggregateId().value(),
                domain.getPayload().value(),
                domain.getPublished().value(),
                domain.getCreated().value()
        );
    }

    public OutboxMessageDto toDto(OutboxMessage domain) {
        return new OutboxMessageDto(
                domain.id().value(),
                domain.aggregateId().value(),
                domain.domain().value(),
                domain.event().value()
        );
    }

    public OutboxMessage toDomain(OutboxMessageDto dto) {
        return new OutboxMessage(
                new OutboxId(dto.id()),
                new OutboxAggregateId(dto.aggregateId()),
                Domain.from(dto.domain(), registry),
                Event.from(dto.event(), registry)
        );
    }
}
