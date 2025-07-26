package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.shared.util.Ensure;

public record OutboxReplayQuery(OutboxSequence sequence, int limit) {
    public OutboxReplayQuery {
        Ensure.notNull(sequence, "OutboxSequence must not be null");
        Ensure.min(1, limit, "Limit must be greater than 0");
        Ensure.max(500, limit, "Limit must be less than 500");
    }
}
