package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public final class EventRegistry implements Registry<Event, String> {

    private static final Logger log = LoggerFactory.getLogger(EventRegistry.class);

    private final Cache<Event, String> registry;

    @SuppressWarnings("unchecked")
    public EventRegistry(CacheManager cacheManager) {
        this.registry = (Cache<Event, String>) cacheManager.getCache(CacheName.EVENT_REGISTRY)
                .getNativeCache();
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
