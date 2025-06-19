package com.github.mangila.webshop.event;

import com.github.mangila.webshop.event.model.Event;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventResolver {

    private final EventServiceGateway eventServiceGateway;

    public EventResolver(EventServiceGateway eventServiceGateway) {
        this.eventServiceGateway = eventServiceGateway;
    }

    @QueryMapping
    public List<Event> replay(
            @Argument("topic") String topic,
            @Argument("aggregateId") String aggregateId,
            @Argument("offset") Long offset,
            @Argument("limit") Integer limit) {
        return eventServiceGateway.replay(topic, aggregateId, offset, limit);
    }

}
