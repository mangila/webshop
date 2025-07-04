package com.github.mangila.webshop.backend.product.infrastructure.config;

import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventRegistryGateway;
import com.github.mangila.webshop.backend.product.domain.event.ProductTopic;
import com.github.mangila.webshop.backend.product.domain.event.ProductEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class ProductConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductConfig.class);

    private final OutboxEventRegistryGateway outboxEventRegistryGateway;

    public ProductConfig(OutboxEventRegistryGateway outboxEventRegistryGateway) {
        this.outboxEventRegistryGateway = outboxEventRegistryGateway;
    }

    @PostConstruct
    public void registerProductEvent() {
        EnumSet.allOf(ProductEvent.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductEvent::name,
                        ProductEvent::name))
                .forEach((key, value) -> {
                    log.info("Registering event: {}", key);
                    outboxEventRegistryGateway.registry().registerType(key, value);
                });
    }

    @PostConstruct
    public void registerProductTopic() {
        EnumSet.allOf(ProductTopic.class)
                .stream()
                .collect(Collectors.toMap(
                        ProductTopic::name,
                        ProductTopic::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    outboxEventRegistryGateway.registry().registerTopic(key, value);
                });
    }
}
