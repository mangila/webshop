package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.model.Event;

import java.util.List;
import java.util.Objects;

public class EventRegistry implements Registry<Event, String> {

    private final Cache<Event, String> registry;

    public EventRegistry(Cache<Event, String> cache) {
        this.registry = cache;
    }

    @Override
    public boolean isRegistered(Event key) {
        return Objects.nonNull(registry.getIfPresent(key));
    }

    @Override
    public String get(Event key) {
        return registry.getIfPresent(key);
    }

    @Override
    public void register(Event key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.asMap().values().stream().toList();
    }

    @Override
    public List<Event> keys() {
        return registry.asMap().keySet().stream().toList();
    }
}
