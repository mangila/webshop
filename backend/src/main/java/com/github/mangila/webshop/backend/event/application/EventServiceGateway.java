package com.github.mangila.webshop.backend.event.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceGateway {

    private final EventReplayService replayer;
    private final EventPublishService publisher;

    public EventServiceGateway(EventReplayService replayer,
                               EventPublishService publisher) {
        this.publisher = publisher;
        this.replayer = replayer;
    }

    public Event routePublish(String topic,
                              String eventType,
                              ApplicationUuid aggregateId,
                              JsonNode payload) {
        return publisher.publish(topic, eventType, aggregateId, payload);
    }

    public List<Event> routeReplay(EventReplayQuery replay) {
        return replayer.replay(replay);
    }
}
