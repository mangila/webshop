package com.github.mangila.webshop.event.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventCommandRepository commandRepository;

    public EventCommandService(EventCommandRepository commandRepository) {
        this.commandRepository = commandRepository;
    }

    public Event emit(EventTopic eventTopic,
                      String aggregateId,
                      String type,
                      JsonNode data) {
        var event = toEvent(
                eventTopic,
                aggregateId,
                type,
                data
        );
        return commandRepository.emit(event);
    }

    private Event toEvent(EventTopic eventTopic,
                          String aggregateId,
                          String eventType,
                          JsonNode data) {
        var pgEvent = new Event();
        pgEvent.setAggregateId(aggregateId);
        pgEvent.setTopic(eventTopic.toString());
        pgEvent.setType(eventType);
        pgEvent.setData(data.toString());
        return pgEvent;
    }
}
