package com.github.mangila.webshop.backend.outboxevent.domain.query;


import java.util.UUID;

public record OutboxEventReplayQuery(
        String topic,
        UUID aggregateId,
        long offset,
        int limit
) {
}
