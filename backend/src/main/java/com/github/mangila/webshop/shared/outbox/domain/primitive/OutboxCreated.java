package com.github.mangila.webshop.shared.outbox.domain.primitive;

import java.time.Instant;

public record OutboxCreated(Instant value) {
}
