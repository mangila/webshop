package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import org.springframework.stereotype.Component;

@Component
public class OutboxDtoMapper {

    public OutboxDto toDto(Outbox domain) {
        return OutboxDto.from(
                domain.getId(),
                domain.getTopic(),
                domain.getEvent(),
                domain.getAggregateId(),
                domain.getPayload(),
                domain.isPublished(),
                domain.getCreated()
        );
    }
}
