package com.github.mangila.webshop.backend.event;

import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.model.EventType;
import com.github.mangila.webshop.backend.product.model.ProductEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventTypeRegistry {

    private static final Logger log = LoggerFactory.getLogger(EventTypeRegistry.class);

    private final Map<EventTopic, Map<String, EventType>> registry;

    public EventTypeRegistry(Map<EventTopic, Map<String, EventType>> registry) {
        this.registry = registry;
        register(EventTopic.PRODUCT, ProductEventType.values());
    }

    private void register(EventTopic topic, EventType[] types) {
        log.info("Registering event types for topic: '{}' - {}", topic, Arrays.toString(types));
        var map = Arrays.stream(types)
                .collect(Collectors.toMap(
                        EventType::type,
                        Function.identity()
                ));
        registry.put(topic, map);
    }

    public EventType get(EventTopic topic, String type) {
        return registry.get(topic).get(type);
    }
}
