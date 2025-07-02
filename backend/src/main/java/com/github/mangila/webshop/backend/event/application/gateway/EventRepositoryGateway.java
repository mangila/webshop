package com.github.mangila.webshop.backend.event.application.gateway;

import com.github.mangila.webshop.backend.event.infrastructure.EventCommandRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventQueryRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventSubscriberRepository;
import org.springframework.stereotype.Service;

@Service
public class EventRepositoryGateway {

    private final EventCommandRepository eventCommandRepository;
    private final EventQueryRepository eventQueryRepository;
    private final EventSubscriberRepository eventSubscriberRepository;

    public EventRepositoryGateway(EventCommandRepository eventCommandRepository,
                                  EventQueryRepository eventQueryRepository,
                                  EventSubscriberRepository eventSubscriberRepository) {
        this.eventCommandRepository = eventCommandRepository;
        this.eventQueryRepository = eventQueryRepository;
        this.eventSubscriberRepository = eventSubscriberRepository;
    }

    public EventCommandRepository command() {
        return eventCommandRepository;
    }

    public EventQueryRepository query() {
        return eventQueryRepository;
    }

    public EventSubscriberRepository subscriber() {
        return eventSubscriberRepository;
    }
}
