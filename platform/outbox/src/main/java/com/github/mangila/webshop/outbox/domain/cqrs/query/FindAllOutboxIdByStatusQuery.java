package com.github.mangila.webshop.outbox.domain.cqrs.query;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;

public record FindAllOutboxIdByStatusQuery(OutboxStatusType status,
                                           int limit) {
    private static final int MIN_LIMIT = 1;
    private static final int DEFAULT_LIMIT = 50;
    private static final int MAX_LIMIT = 500;

    private static final FindAllOutboxIdByStatusQuery PENDING_DEFAULT = new FindAllOutboxIdByStatusQuery(OutboxStatusType.PENDING, DEFAULT_LIMIT);
    private static final FindAllOutboxIdByStatusQuery PUBLISHED_DEFAULT = new FindAllOutboxIdByStatusQuery(OutboxStatusType.PUBLISHED, DEFAULT_LIMIT);

    public FindAllOutboxIdByStatusQuery {
        Ensure.notNull(status, OutboxStatusType.class);
        Ensure.min(MIN_LIMIT, limit);
        Ensure.max(MAX_LIMIT, limit);
    }

    public static FindAllOutboxIdByStatusQuery pending() {
        return PENDING_DEFAULT;
    }

    public static FindAllOutboxIdByStatusQuery published() {
        return PUBLISHED_DEFAULT;
    }

}
