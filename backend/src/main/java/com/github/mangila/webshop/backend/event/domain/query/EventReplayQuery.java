package com.github.mangila.webshop.backend.event.domain.query;


import java.util.UUID;

public record EventReplayQuery(
        String topic,
        UUID aggregateId,
        long offset,
        int limit
) {
}
