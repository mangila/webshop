package com.github.mangila.webshop.backend.event.application.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.event.application.service.EventPublishService;
import com.github.mangila.webshop.backend.event.application.service.EventReplayService;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventServiceGateway {

    private final EventReplayService replayer;
    private final EventPublishService publisher;

    public EventServiceGateway(EventReplayService replayer,
                               EventPublishService publisher) {
        this.publisher = publisher;
        this.replayer = replayer;
    }

    public Event publish(String topic,
                         String eventType,
                         UUID aggregateId,
                         JsonNode payload) {
        return publisher.publish(topic, eventType, aggregateId, payload);
    }

    public List<Event> replay(EventReplayQuery replay) {
        return replayer.replay(replay);
    }
}
