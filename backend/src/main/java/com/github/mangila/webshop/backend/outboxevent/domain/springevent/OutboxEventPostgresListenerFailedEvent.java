package com.github.mangila.webshop.backend.outboxevent.domain.springevent;

public record OutboxEventPostgresListenerFailedEvent(Throwable cause) {
}
