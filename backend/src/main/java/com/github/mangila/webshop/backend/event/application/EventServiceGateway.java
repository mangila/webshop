package com.github.mangila.webshop.backend.event.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.event.domain.command.EventEmitCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventTopic;
import com.github.mangila.webshop.backend.event.domain.model.EventType;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
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
                      String eventType,
                      ApplicationUuid aggregateId,
                      JsonNode eventData) {
        return commandService.emit(new EventEmitCommand(
                topic,
                new EventType(eventType),
                aggregateId,
                eventData
        ));
    }

    public List<Event> replay(EventReplayQuery replay) {
        return queryService.replay(replay);
    }
}
