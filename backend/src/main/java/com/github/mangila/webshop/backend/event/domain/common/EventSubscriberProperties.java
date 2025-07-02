package com.github.mangila.webshop.backend.event.domain.common;

import com.github.mangila.webshop.backend.event.domain.command.EventSubscribeCommand;

public record EventSubscriberProperties(
        String consumer,
        String topic,
        String type
) {
    public EventSubscribeCommand toCommand() {
        return new EventSubscribeCommand(consumer, topic, type);
    }
}
