package com.github.mangila.webshop.backend.event.domain.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventTopic;
import com.github.mangila.webshop.backend.event.domain.model.EventType;

public record EventEmitCommand(EventTopic eventTopic, EventType eventType, ApplicationUuid aggregateId,
                               JsonNode eventData) {

    public Event toNewEvent() {
        return new Event(eventTopic, eventType, aggregateId, eventData);
    }

}
