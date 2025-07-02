package com.github.mangila.webshop.backend.product.config;

import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventType;
import com.github.mangila.webshop.backend.product.domain.event.ProductTopicType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class ProductConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductConfig.class);

    private final EventRegistryGateway eventRegistryGateway;

    public ProductConfig(EventRegistryGateway eventRegistryGateway) {
        this.eventRegistryGateway = eventRegistryGateway;
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
                    eventRegistryGateway.registerEventType(key, value);
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
                    eventRegistryGateway.registerEventTopic(key, value);
                });
    }
}
