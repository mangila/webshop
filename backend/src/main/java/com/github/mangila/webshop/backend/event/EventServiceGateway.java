package com.github.mangila.webshop.backend.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.event.command.EventCommandService;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.query.EventQueryService;
import com.github.mangila.webshop.backend.event.query.model.EventQueryReplay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceGateway {

    private final EventQueryService queryService;
    private final EventCommandService commandService;

    public EventServiceGateway(EventQueryService queryService,
                               EventCommandService commandService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    public Event emit(EventTopic topic,
                      String aggregateId,
                      String eventType,
                      JsonNode eventData) {
        return commandService.emit(
                topic,
                aggregateId,
                eventType,
                eventData
        );
    }

    public List<Event> replay(EventQueryReplay replay) {
        return queryService.replay(replay);
    }
}
