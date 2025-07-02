package com.github.mangila.webshop.backend.event.application.gateway;

import com.github.mangila.webshop.backend.event.application.service.EventPublishService;
import com.github.mangila.webshop.backend.event.application.service.EventReplayService;
import com.github.mangila.webshop.backend.event.application.service.EventSubscribeService;
import org.springframework.stereotype.Service;

@Service
public class EventServiceGateway {

    private final EventReplayService replayer;
    private final EventPublishService publisher;
    private final EventSubscribeService subscriber;

    public EventServiceGateway(EventReplayService replayer,
                               EventPublishService publisher,
                               EventSubscribeService subscriber) {
        this.publisher = publisher;
        this.replayer = replayer;
        this.subscriber = subscriber;
    }

    public EventReplayService replayer() {
        return replayer;
    }

    public EventPublishService publisher() {
        return publisher;
    }

    public EventSubscribeService subscriber() {
        return subscriber;
    }
}
