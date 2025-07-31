package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.annotation.Event;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.model.CacheName;
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
public class EventRegistry implements Registry<com.github.mangila.webshop.shared.model.Event, String> {

    private static final Logger log = LoggerFactory.getLogger(EventRegistry.class);
    private final Cache<com.github.mangila.webshop.shared.model.Event, String> registry;

    @SuppressWarnings("unchecked")
    public EventRegistry(CacheManager cacheManager) {
        this.registry = (Cache<com.github.mangila.webshop.shared.model.Event, String>) cacheManager.getCache(CacheName.EVENT_REGISTRY)
                .getNativeCache();
    }

    @PostConstruct
    public void init() {
        scanEventAnnotations();
    }

    private void scanEventAnnotations() {
        log.info("Scanning for @Event annotated enums");
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));
        reflections.getTypesAnnotatedWith(Event.class)
                .forEach(eventClazz -> {
                    if (eventClazz.isEnum()) {
                        var enumConstants = eventClazz.getEnumConstants();
                        Ensure.notEmpty(enumConstants, "Enums cannot be empty");
                        for (Object enumValue : enumConstants) {
                            Enum<?> e = (Enum<?>) enumValue;
                            log.info("Register event: {}", e.name());
                            var event = new com.github.mangila.webshop.shared.model.Event(e);
                            register(event, event.value());
                        }
                    } else {
                        throw new ApplicationException(String.format("@Event %s must be an enum", eventClazz.getName()));
                    }
                });
    }

    @Override
    public boolean isRegistered(com.github.mangila.webshop.shared.model.Event key) {
        return Objects.nonNull(registry.getIfPresent(key));
    }

    @Override
    public void ensureIsRegistered(com.github.mangila.webshop.shared.model.Event key) {
        if (!isRegistered(key)) {
            throw new ApplicationException(String.format("Event %s is not registered", key));
        }
    }

    @Override
    public String get(com.github.mangila.webshop.shared.model.Event key) {
        return registry.getIfPresent(key);
    }

    @Override
    public void register(com.github.mangila.webshop.shared.model.Event key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.asMap().values().stream().toList();
    }

    @Override
    public List<com.github.mangila.webshop.shared.model.Event> keys() {
        return registry.asMap().keySet().stream().toList();
    }
}
