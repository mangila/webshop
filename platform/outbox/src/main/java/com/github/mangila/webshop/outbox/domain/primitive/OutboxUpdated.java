package com.github.mangila.webshop.outbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record OutboxUpdated(Instant value) {
    public OutboxUpdated {
        Ensure.notNull(value, "Instant must not be null");
    }

    public static OutboxUpdated now() {
        return new OutboxUpdated(Instant.now());
    }
}
