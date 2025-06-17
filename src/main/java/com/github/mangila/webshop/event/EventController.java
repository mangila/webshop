package com.github.mangila.webshop.event;

import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.query.EventQueryService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventController {

    private final EventQueryService eventQueryService;

    public EventController(EventQueryService eventQueryService) {
        this.eventQueryService = eventQueryService;
    }

    @QueryMapping
    public List<Event> replay(
            @Argument("topic") String topic,
            @Argument("aggregateId") String aggregateId,
            @Argument("offset") Long offset,
            @Argument("limit") Integer limit) {
        return eventQueryService.replay(topic, aggregateId, offset, limit);
    }

}
