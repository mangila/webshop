package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import org.springframework.stereotype.Component;

@Component
public class OutboxDtoMapper {

    public OutboxDto toDto(Outbox domain) {
        return OutboxDto.from(
                domain.getId().value(),
                domain.getTopic().value(),
                domain.getEvent().value(),
                domain.getAggregateId().value(),
                domain.getPayload().value(),
                domain.getPublished().value(),
                domain.getCreated().value()
        );
    }
}
