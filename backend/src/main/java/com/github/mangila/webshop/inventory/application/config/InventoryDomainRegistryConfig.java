package com.github.mangila.webshop.inventory.application.config;

import com.github.mangila.webshop.inventory.domain.event.InventoryEvent;
import com.github.mangila.webshop.inventory.domain.model.Inventory;
import com.github.mangila.webshop.shared.application.registry.DomainKey;
import com.github.mangila.webshop.shared.application.registry.EventKey;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.EnumSet;

@Configuration
public class InventoryDomainRegistryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryDomainRegistryConfig.class);

    public static final DomainKey INVENTORY_DOMAIN_KEY = DomainKey.create(Inventory.class);

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
        log.info("Registering domain: {}", INVENTORY_DOMAIN_KEY.value());
        registryService.registerDomain(INVENTORY_DOMAIN_KEY, INVENTORY_DOMAIN_KEY.value());
    }

    void registerInventoryEvent() {
        EnumSet.allOf(InventoryEvent.class)
                .stream()
                .map(EventKey::create)
                .forEach(eventKey -> {
                    log.info("Registering event: {}", eventKey.value());
                    registryService.registerEvent(eventKey, eventKey.value());
                });
    }
}
