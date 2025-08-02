package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.model.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class EventRegistry implements Registry<Event, String> {
    private final Map<Event, String> registry;

    public EventRegistry(Map<Event, String> eventToName) {
        this.registry = eventToName;
    }

    @Override
    public boolean isRegistered(Event key) {
        return registry.containsKey(key);
    }

    @Override
    public String get(Event key) {
        return registry.get(key);
    }

    @Override
    public void register(Event key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values().stream().toList();
    }

    @Override
    public List<Event> keys() {
        return registry.keySet().stream().toList();
    }
}
