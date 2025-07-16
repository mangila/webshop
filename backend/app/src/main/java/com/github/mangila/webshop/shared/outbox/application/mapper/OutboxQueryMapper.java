package com.github.mangila.webshop.shared.outbox.application.mapper;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdQuery;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

@Component
public class OutboxQueryMapper {

    public OutboxId toDomain(OutboxIdQuery query) {
        return new OutboxId(query.id());
    }
}
