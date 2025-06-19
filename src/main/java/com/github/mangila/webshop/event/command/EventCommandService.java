package com.github.mangila.webshop.event.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventEntity;
import com.github.mangila.webshop.event.model.EventTopic;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventCommandRepository commandRepository;

    public EventCommandService(EventCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Event emit(EventTopic topic,
                      String aggregateId,
                      String eventType,
                      JsonNode eventData) {
        EventEntity entity = EventEntity.from(topic, aggregateId, eventType, eventData);
        return commandRepository.emit(entity).orElseThrow();
    }
}
