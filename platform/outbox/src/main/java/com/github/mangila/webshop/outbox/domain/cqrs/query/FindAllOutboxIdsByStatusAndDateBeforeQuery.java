package com.github.mangila.webshop.outbox.domain.cqrs.query;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record FindAllOutboxIdsByStatusAndDateBeforeQuery(OutboxStatusType status,
                                                         Instant date,
                                                         int limit) {
    public FindAllOutboxIdsByStatusAndDateBeforeQuery {
        Ensure.notNull(status, OutboxStatusType.class);
        Ensure.notNull(date, Instant.class);
        Ensure.min(1, limit);
    }
}
