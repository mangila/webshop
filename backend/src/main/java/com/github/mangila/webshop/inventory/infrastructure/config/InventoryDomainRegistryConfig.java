package com.github.mangila.webshop.inventory.infrastructure.config;

import com.github.mangila.webshop.inventory.domain.event.InventoryEvent;
import com.github.mangila.webshop.inventory.domain.event.InventoryTopic;
import com.github.mangila.webshop.shared.application.registry.DomainRegistryService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class InventoryDomainRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryDomainRegistryConfig.class);

    private final DomainRegistryService domainRegistryService;

    public InventoryDomainRegistryConfig(DomainRegistryService domainRegistryService) {
        this.domainRegistryService = domainRegistryService;
    }

    @PostConstruct
    public void registerInventoryEvent() {
        log.debug("Registering domain events for inventory domain");
        EnumSet.allOf(InventoryEvent.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryEvent::name,
                        InventoryEvent::name))
                .forEach((key, value) -> {
                    log.info("Registering event: {}", key);
                    domainRegistryService.registerType(key, value);
                });
    }

    @PostConstruct
    public void registerInventoryTopic() {
        log.debug("Registering domain topics for inventory domain");
        EnumSet.allOf(InventoryTopic.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryTopic::name,
                        InventoryTopic::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    domainRegistryService.registerTopic(key, value);
                });
    }
}
