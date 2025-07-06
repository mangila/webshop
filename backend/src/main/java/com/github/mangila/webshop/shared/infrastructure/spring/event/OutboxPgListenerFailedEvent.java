package com.github.mangila.webshop.shared.infrastructure.spring.event;

public record OutboxPgListenerFailedEvent(Throwable cause) {
}
