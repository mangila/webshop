package com.github.mangila.webshop.backend.event.domain.common;

public record EventSubscriberProperties(
        String consumer,
        String topic,
        String type
) {
}
