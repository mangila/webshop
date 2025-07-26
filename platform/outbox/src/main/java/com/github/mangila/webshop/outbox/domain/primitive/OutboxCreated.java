package com.github.mangila.webshop.outbox.domain.primitive;

import com.github.mangila.webshop.shared.util.Ensure;

import java.time.Instant;

public record OutboxCreated(Instant value) {
    public OutboxCreated {
        Ensure.notNull(value, "Instant must not be null");
    }
}
