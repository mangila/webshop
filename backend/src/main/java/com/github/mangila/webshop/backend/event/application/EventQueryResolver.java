package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventQueryResolver {

    private final EventServiceGateway eventServiceGateway;

    public EventQueryResolver(EventServiceGateway eventServiceGateway) {
        this.eventServiceGateway = eventServiceGateway;
    }

    @QueryMapping
    public List<Event> replay(@Argument("input") @Valid EventReplayQuery replay) {
        return eventServiceGateway.routeReplay(replay);
    }

}
