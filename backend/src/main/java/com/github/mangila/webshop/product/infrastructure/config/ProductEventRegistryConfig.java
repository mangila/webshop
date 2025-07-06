package com.github.mangila.webshop.product.infrastructure.config;

import com.github.mangila.webshop.product.infrastructure.event.ProductEvent;
import com.github.mangila.webshop.product.infrastructure.event.ProductTopic;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRegistryGateway;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class ProductEventRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(ProductEventRegistryConfig.class);

    private final OutboxRegistryGateway outboxRegistryGateway;

    public ProductEventRegistryConfig(OutboxRegistryGateway outboxRegistryGateway) {
        this.outboxRegistryGateway = outboxRegistryGateway;
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
                    outboxRegistryGateway.registry().registerType(key, value);
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
                    outboxRegistryGateway.registry().registerTopic(key, value);
                });
    }
}
