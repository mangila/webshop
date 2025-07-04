package com.github.mangila.webshop.backend.outboxevent.application.registry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DefaultTopicRegistry implements EventRegistry {

    private final Map<String, String> registry;

    public DefaultTopicRegistry(@Qualifier("topicRegistryMap") Map<String, String> registry) {
        this.registry = registry;
    }

    @Override
    public boolean isRegistered(String key) {
        return registry.containsKey(key);
    }

    @Override
    public void register(String key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values()
                .stream()
                .toList();
    }
}
