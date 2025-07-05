package com.github.mangila.webshop.shared.outbox.application.cqrs;


import java.util.UUID;

public record OutboxReplayQuery(
        String topic,
        UUID aggregateId,
        long offset,
        int limit
) {
}
