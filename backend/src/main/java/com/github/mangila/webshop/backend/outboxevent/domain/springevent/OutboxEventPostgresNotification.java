package com.github.mangila.webshop.backend.outboxevent.domain.springevent;

import org.postgresql.PGNotification;

public record OutboxEventPostgresNotification(PGNotification notification) {
}
