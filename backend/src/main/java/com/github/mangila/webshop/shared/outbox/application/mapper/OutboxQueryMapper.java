package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxFindByIdQuery;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxFindById;
import org.springframework.stereotype.Component;

@Component
public class OutboxQueryMapper {

    public OutboxFindById toDomain(OutboxFindByIdQuery query) {
        return OutboxFindById.from(query.id());
    }
}
