package com.github.mangila.webshop.event.query;

import com.github.mangila.webshop.event.EventServiceGateway;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.query.model.EventQueryReplay;
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
    public List<Event> replay(@Argument("input") @Valid EventQueryReplay replay) {
        return eventServiceGateway.replay(replay);
    }

}
