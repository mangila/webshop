package com.github.mangila.webshop.outbox.domain.cqrs.query;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;

public record FindAllOutboxIdByStatusQuery(OutboxStatusType status,
                                           int limit) {
    public FindAllOutboxIdByStatusQuery {
        Ensure.notNull(status, OutboxStatusType.class);
        Ensure.min(1, limit);
    }

    public static FindAllOutboxIdByStatusQuery pending(int limit) {
        return new FindAllOutboxIdByStatusQuery(OutboxStatusType.PENDING, limit);
    }

    public static FindAllOutboxIdByStatusQuery published(int limit) {
        return new FindAllOutboxIdByStatusQuery(OutboxStatusType.PUBLISHED, limit);
    }

}
