package com.github.mangila.webshop.inventory.application.config;

import com.github.mangila.webshop.inventory.domain.event.InventoryEvent;
import com.github.mangila.webshop.inventory.domain.model.Inventory;
import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class InventoryDomainRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryDomainRegistryConfig.class);

    private final RegistryService registryService;

    public InventoryDomainRegistryConfig(RegistryService registryService) {
        this.registryService = registryService;
    }

    @PostConstruct
    void init() {
        registerInventoryDomain();
        registerInventoryEvent();
    }

    void registerInventoryDomain() {
        final Domain domain = Domain.from(Inventory.class);
        log.info("Registering domain: {}", domain.value());
        registryService.register(domain);
    }

    void registerInventoryEvent() {
        EnumSet.allOf(InventoryEvent.class)
                .stream()
                .map(Event::from)
                .forEach(event -> {
                    log.info("Registering event: {}", event.value());
                    registryService.register(event);
                });
    }
}
