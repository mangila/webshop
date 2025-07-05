package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.outbox.infrastructure.registry.EventRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxRegistryService {

    private final EventRegistry eventTopicRegistry;
    private final EventRegistry eventTypeRegistry;

    public OutboxRegistryService(@Qualifier("defaultTopicRegistry") EventRegistry eventTopicRegistry,
                                 @Qualifier("defaultEventRegistry") EventRegistry eventTypeRegistry) {
        this.eventTopicRegistry = eventTopicRegistry;
        this.eventTypeRegistry = eventTypeRegistry;
    }

    public void ensureHasTopicAndTypeRegistered(String topic, String type) {
        if (!eventTopicRegistry.isRegistered(topic)) {
            throw new ApplicationException(String.format("Topic is not registered: '%s'", topic));
        }
        if (!eventTypeRegistry.isRegistered(type)) {
            throw new ApplicationException(String.format("Event is not registered: '%s'", type));
        }
    }

    public List<String> topics() {
        return eventTopicRegistry.values();
    }

    public void registerType(String key, String value) {
        eventTypeRegistry.register(key, value);
    }

    public void registerTopic(String key, String value) {
        eventTopicRegistry.register(key, value);
    }
}
