package com.github.mangila.webshop.backend.event.command.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.model.EventType;

public record EventEmitCommand(
        EventTopic eventTopic,
        String aggregateId,
        EventType eventType,
        JsonNode eventData
) {
}
