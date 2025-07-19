package com.github.mangila.webshop.outbox.infrastructure.jpa.projection;

public record OutboxMessageProjection(Long id, String domain, String event) {
}
