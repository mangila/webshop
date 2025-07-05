package com.github.mangila.webshop.outboxevent.application.service;

import com.github.mangila.webshop.shared.domain.exception.WebException;
import com.github.mangila.webshop.outboxevent.application.registry.EventRegistry;
import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxEventRegistryService {

    private final EventRegistry eventTopicRegistry;
    private final EventRegistry eventTypeRegistry;

    public OutboxEventRegistryService(@Qualifier("defaultTopicRegistry") EventRegistry eventTopicRegistry,
                                      @Qualifier("defaultEventRegistry") EventRegistry eventTypeRegistry) {
        this.eventTopicRegistry = eventTopicRegistry;
        this.eventTypeRegistry = eventTypeRegistry;
    }

    public void hasTopicAndTypeRegistered(String topic, String type) {
        if (!eventTopicRegistry.isRegistered(topic)) {
            throw new WebException(String.format("Topic is not registered: '%s'", topic), OutboxEvent.class, HttpStatus.CONFLICT);
        }
        if (!eventTypeRegistry.isRegistered(type)) {
            throw new WebException(String.format("Event is not registered: '%s'", type), OutboxEvent.class, HttpStatus.CONFLICT);
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
