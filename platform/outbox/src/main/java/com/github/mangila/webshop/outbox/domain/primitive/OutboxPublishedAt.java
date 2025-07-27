package com.github.mangila.webshop.outbox.domain.primitive;

import org.jspecify.annotations.Nullable;

import java.time.Instant;

public record OutboxPublishedAt(@Nullable Instant value) {
    public static OutboxPublishedAt now() {
        return new OutboxPublishedAt(Instant.now());
    }
}
