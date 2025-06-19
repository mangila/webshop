package com.github.mangila.webshop.event;

import com.github.mangila.webshop.event.command.EventCommandGateway;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventCommand;
import com.github.mangila.webshop.event.query.EventQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventServiceGateway {

    private final EventQueryService queryService;
    private final EventCommandGateway eventCommandGateway;

    public EventServiceGateway(EventQueryService queryService,
                               EventCommandGateway eventCommandGateway) {
        this.queryService = queryService;
        this.eventCommandGateway = eventCommandGateway;
    }


    @Transactional
    public Event processCommand(EventCommand command) {
        return eventCommandGateway.processCommand(command);
    }

    public List<Event> replay(String topic,
                              String aggregateId,
                              Long offset,
                              Integer limit) {
        return queryService.replay(topic, aggregateId, offset, limit);
    }


}
