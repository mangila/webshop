package com.github.mangila.webshop.backend.outboxevent.domain;


import java.util.UUID;

public record OutboxEventReplayQuery(
        String topic,
        UUID aggregateId,
        long offset,
        int limit
) {
}
