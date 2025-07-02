package com.github.mangila.webshop.backend.product.config;

import com.github.mangila.webshop.backend.event.application.EventRegistry;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventType;
import com.github.mangila.webshop.backend.product.domain.event.ProductTopicType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class ProductConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductConfig.class);

    private final EventRegistry eventTypeRegistry;
    private final EventRegistry eventTopicRegistry;

    public ProductConfig(@Qualifier("defaultEventTypeRegistry") EventRegistry eventTypeRegistry,
                         @Qualifier("defaultEventTopicRegistry") EventRegistry eventTopicRegistry) {
        this.eventTypeRegistry = eventTypeRegistry;
        this.eventTopicRegistry = eventTopicRegistry;
    }

    @PostConstruct
    public void registerEventTypes() {
        log.debug("Registering event types for product domain");
        EnumSet.allOf(ProductEventType.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductEventType::name,
                        ProductEventType::name))
                .forEach((key, value) -> {
                    log.info("Registering event type: {}", key);
                    eventTypeRegistry.register(key, value);
                });
    }

    @PostConstruct
    public void registerTopicTypes() {
        log.debug("Registering event topics for product domain");
        EnumSet.allOf(ProductTopicType.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductTopicType::name,
                        ProductTopicType::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    eventTopicRegistry.register(key, value);
                });
    }
}
