package com.github.mangila.webshop.common.event;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EventController {

    @QueryMapping
    public List<Event> replay(
            @Argument("topic") String topic,
            @Argument("aggregateId") String aggregateId,
            @Argument("offset") Long offset) {
        return List.of();
    }

}
