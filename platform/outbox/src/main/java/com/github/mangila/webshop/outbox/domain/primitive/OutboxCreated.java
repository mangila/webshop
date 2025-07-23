package com.github.mangila.webshop.outbox.domain.primitive;

import java.time.Instant;

public record OutboxCreated(Instant value) {
}
