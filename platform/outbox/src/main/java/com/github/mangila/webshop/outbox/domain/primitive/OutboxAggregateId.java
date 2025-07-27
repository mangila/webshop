package com.github.mangila.webshop.outbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.util.UUID;

public record OutboxAggregateId(UUID value) {
    public OutboxAggregateId {
        Ensure.notNull(value, "UUID must not be null");
    }

}
