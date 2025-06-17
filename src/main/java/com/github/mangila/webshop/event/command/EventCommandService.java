package com.github.mangila.webshop.event.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import com.github.mangila.webshop.event.model.exception.EventEmitException;
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
        var event = Event.from(
                eventTopic,
                aggregateId,
                type,
                data
        );
        return commandRepository.emit(event)
                .orElseThrow(() -> new EventEmitException(event.toString()));
    }
}
