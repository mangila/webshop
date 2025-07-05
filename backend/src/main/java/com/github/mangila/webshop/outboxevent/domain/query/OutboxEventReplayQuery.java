package com.github.mangila.webshop.outboxevent.domain.query;


import java.util.UUID;

public record OutboxEventReplayQuery(
        String topic,
        UUID aggregateId,
        long offset,
        int limit
) {
}
