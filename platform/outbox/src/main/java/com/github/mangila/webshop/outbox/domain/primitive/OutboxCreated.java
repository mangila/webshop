package com.github.mangila.webshop.outbox.domain.primitive;

import com.github.mangila.webshop.shared.Ensure;

import java.time.Instant;

public record OutboxCreated(Instant value) {
    public OutboxCreated {
        Ensure.notNull(value, Instant.class);
    }
}
