package com.github.mangila.webshop.backend.product.config;

import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventTopicType;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventType;
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
    public void registerProductEventTypes() {
        log.debug("Registering event types for product domain");
        EnumSet.allOf(ProductEventType.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductEventType::name,
                        ProductEventType::name))
                .forEach((key, value) -> {
                    log.info("Registering event type: {}", key);
                    eventRegistryGateway.eventTypeRegistry().register(key, value);
                });
    }

    @PostConstruct
    public void registerProductEventTopicTypes() {
        log.debug("Registering event topics for product domain");
        EnumSet.allOf(ProductEventTopicType.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductEventTopicType::name,
                        ProductEventTopicType::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    eventRegistryGateway.eventTopicRegistry().register(key, value);
                });
    }
}
