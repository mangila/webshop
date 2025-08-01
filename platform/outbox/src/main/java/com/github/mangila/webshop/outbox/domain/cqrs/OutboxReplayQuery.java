package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.shared.Ensure;

public record OutboxReplayQuery(OutboxSequence sequence, int limit) {
    public OutboxReplayQuery {
        Ensure.notNull(sequence, OutboxSequence.class);
        Ensure.min(1, limit);
        Ensure.max(500, limit);
    }
}
