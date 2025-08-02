package com.github.mangila.webshop.app.config;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.annotation.EventTypes;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class EventRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(EventRegistryConfig.class);

    @Bean
    Map<Event, String> eventToName() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    EventRegistry eventRegistry(Map<Event, String> eventToName) {
        var eventRegistry = new EventRegistry(eventToName);
        registerEvents(eventRegistry);
        return eventRegistry;
    }

    void registerEvents(EventRegistry eventRegistry) {
        Class<EventTypes> annotation = EventTypes.class;
        log.info("Scan {}", annotation.getName());
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forJavaClassPath())
                .setScanners(Scanners.TypesAnnotated));
        reflections.getTypesAnnotatedWith(annotation)
                .forEach(eventClazz -> {
                    if (eventClazz.isEnum()) {
                        var enumConstants = eventClazz.getEnumConstants();
                        Ensure.notEmpty(enumConstants, eventClazz);
                        for (Object enumValue : enumConstants) {
                            Enum<?> e = (Enum<?>) enumValue;
                            log.info("Register event: {}", e.name());
                            var event = new Event(e);
                            eventRegistry.register(event, event.value());
                        }
                    } else {
                        throw new ApplicationException("Class %s is not an enum".formatted(eventClazz.getSimpleName()));
                    }
                });
    }
}
