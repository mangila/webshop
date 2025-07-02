package com.github.mangila.webshop.backend.event.application.gateway;

import com.github.mangila.webshop.backend.event.application.registry.EventRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventRegistryGateway {

    private final EventRegistry eventTypeRegistry;
    private final EventRegistry eventTopicRegistry;

    public EventRegistryGateway(@Qualifier("defaultEventTypeRegistry") EventRegistry eventTypeRegistry,
                                @Qualifier("defaultEventTopicRegistry") EventRegistry eventTopicRegistry) {
        this.eventTypeRegistry = eventTypeRegistry;
        this.eventTopicRegistry = eventTopicRegistry;
    }

    public EventRegistry eventTypeRegistry() {
        return eventTypeRegistry;
    }

    public EventRegistry eventTopicRegistry() {
        return eventTopicRegistry;
    }

}
