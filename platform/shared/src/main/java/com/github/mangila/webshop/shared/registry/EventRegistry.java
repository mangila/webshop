package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.annotation.EventTypes;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.Event;
import jakarta.annotation.PostConstruct;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
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

    @PostConstruct
    public void init() {
        scanEventTypes();
    }

    private void scanEventTypes() {
        Class<EventTypes> annotation = EventTypes.class;
        log.info("Scan {}", annotation.getName());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));
        reflections.getTypesAnnotatedWith(annotation)
                .forEach(eventClazz -> {
                    if (eventClazz.isEnum()) {
                        var enumConstants = eventClazz.getEnumConstants();
                        Ensure.notEmpty(enumConstants, "Enums cannot be empty");
                        for (Object enumValue : enumConstants) {
                            Enum<?> e = (Enum<?>) enumValue;
                            log.info("Register event: {}", e.name());
                            var event = new Event(e);
                            register(event, event.value());
                        }
                    } else {
                        throw new ApplicationException("Class %s is not an enum".formatted(eventClazz.getSimpleName()));
                    }
                });
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
