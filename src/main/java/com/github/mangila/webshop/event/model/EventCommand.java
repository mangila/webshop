package com.github.mangila.webshop.event.model;

public record EventCommand(
        EventCommandType commandType,
        String topic,
        String eventType,
        String aggregateId,
        String data
) {
}
