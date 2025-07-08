package com.github.mangila.webshop.shared.application.registry;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@ObservedService
public class DomainRegistryService {

    private final DomainRegistry topicRegistry;
    private final DomainRegistry eventRegistry;

    public DomainRegistryService(@Qualifier("defaultTopicDomainRegistry") DomainRegistry eventTopicRegistry,
                                 @Qualifier("defaultEventDomainRegistry") DomainRegistry eventRegistry) {
        this.topicRegistry = eventTopicRegistry;
        this.eventRegistry = eventRegistry;
    }

    public void ensureHasTopicAndTypeRegistered(String topic, String type) {
        if (!topicRegistry.isRegistered(topic)) {
            throw new ApplicationException(String.format("Topic is not registered: '%s'", topic));
        }
        if (!eventRegistry.isRegistered(type)) {
            throw new ApplicationException(String.format("Event is not registered: '%s'", type));
        }
    }

    public List<String> topics() {
        return topicRegistry.values();
    }

    public void registerType(String key, String value) {
        eventRegistry.register(key, value);
    }

    public void registerTopic(String key, String value) {
        topicRegistry.register(key, value);
    }
}
