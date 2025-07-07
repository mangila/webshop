package com.github.mangila.webshop.shared.application.registry;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultTopicDomainRegistry implements DomainRegistry {

    private final Map<String, String> registry;

    public DefaultTopicDomainRegistry() {
        this.registry = new ConcurrentHashMap<>();
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
