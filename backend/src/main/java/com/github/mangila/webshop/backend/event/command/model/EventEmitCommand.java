package com.github.mangila.webshop.backend.event.command.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.event.model.EventTopic;

public record EventEmitCommand(
        EventTopic eventTopic,
        String aggregateId,
        String eventType,
        JsonNode eventData
) {
}
