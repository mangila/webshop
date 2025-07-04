package com.github.mangila.webshop.backend.inventory.infrastructure.config;

import com.github.mangila.webshop.backend.inventory.domain.event.InventoryTopic;
import com.github.mangila.webshop.backend.inventory.domain.event.InventoryEvent;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventRegistryGateway;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class InventoryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryConfig.class);

    private final OutboxEventRegistryGateway outboxEventRegistryGateway;

    public InventoryConfig(OutboxEventRegistryGateway outboxEventRegistryGateway) {
        this.outboxEventRegistryGateway = outboxEventRegistryGateway;
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
                    outboxEventRegistryGateway.registry().registerType(key, value);
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
                    outboxEventRegistryGateway.registry().registerTopic(key, value);
                });
    }
}
