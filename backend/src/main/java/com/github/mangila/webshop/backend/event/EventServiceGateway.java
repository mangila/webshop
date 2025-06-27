package com.github.mangila.webshop.backend.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.event.command.EventCommandService;
import com.github.mangila.webshop.backend.event.command.model.EventEmitCommand;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.model.EventType;
import com.github.mangila.webshop.backend.event.query.EventQueryService;
import com.github.mangila.webshop.backend.event.query.model.EventReplayQuery;
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
                      EventType eventType,
                      JsonNode eventData) {
        return commandService.emit(new EventEmitCommand(
                topic,
                aggregateId,
                eventType,
                eventData
        ));
    }

    public List<Event> replay(EventReplayQuery replay) {
        return queryService.replay(replay);
    }
}
