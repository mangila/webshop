package com.github.mangila.webshop.backend.event.application.gateway;

import com.github.mangila.webshop.backend.event.infrastructure.EventCommandRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventQueryRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventSubscriberCommandRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventSubscriberQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class EventRepositoryGateway {

    private final EventCommandRepository eventCommand;
    private final EventQueryRepository eventQuery;
    private final EventSubscriberCommandRepository subscriberCommand;
    private final EventSubscriberQueryRepository subscriberQuery;

    public EventRepositoryGateway(EventCommandRepository eventCommand,
                                  EventQueryRepository eventQuery,
                                  EventSubscriberCommandRepository subscriberCommand,
                                  EventSubscriberQueryRepository subscriberQuery) {
        this.eventCommand = eventCommand;
        this.eventQuery = eventQuery;
        this.subscriberCommand = subscriberCommand;
        this.subscriberQuery = subscriberQuery;
    }

    public EventCommandRepository eventCommand() {
        return eventCommand;
    }

    public EventQueryRepository eventQuery() {
        return eventQuery;
    }

    public EventSubscriberCommandRepository subscriberCommand() {
        return subscriberCommand;
    }

    public EventSubscriberQueryRepository subscriberQuery() {
        return subscriberQuery;
    }
}
