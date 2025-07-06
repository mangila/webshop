package com.github.mangila.webshop.shared.outbox.infrastructure.message;

public record OutboxMessage(long id, String topic, String event) {
}
