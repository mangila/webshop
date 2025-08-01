package com.github.mangila.webshop.outbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record OutboxUpdated(Instant value) {
    public OutboxUpdated {
        Ensure.notNull(value, Instant.class);
    }

    public static OutboxUpdated now() {
        return new OutboxUpdated(Instant.now());
    }
}
