package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.inventory.domain.event.InventoryEvent;
import com.github.mangila.webshop.inventory.domain.event.InventoryTopic;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRegistryGateway;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class InventoryEventRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventRegistryConfig.class);

    private final OutboxRegistryGateway outboxRegistryGateway;

    public InventoryEventRegistryConfig(OutboxRegistryGateway outboxRegistryGateway) {
        this.outboxRegistryGateway = outboxRegistryGateway;
    }

    @PostConstruct
    public void registerInventoryEvent() {
        log.debug("Registering event for inventory domain");
        EnumSet.allOf(InventoryEvent.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryEvent::name,
                        InventoryEvent::name))
                .forEach((key, value) -> {
                    log.info("Registering event: {}", key);
                    outboxRegistryGateway.registry().registerType(key, value);
                });
    }

    @PostConstruct
    public void registerInventoryTopic() {
        log.debug("Registering topics for inventory domain");
        EnumSet.allOf(InventoryTopic.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryTopic::name,
                        InventoryTopic::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    outboxRegistryGateway.registry().registerTopic(key, value);
                });
    }
}
