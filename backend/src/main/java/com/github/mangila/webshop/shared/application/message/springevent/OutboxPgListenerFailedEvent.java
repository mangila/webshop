package com.github.mangila.webshop.shared.application.message.springevent;

public record OutboxPgListenerFailedEvent(Throwable cause) {
}
