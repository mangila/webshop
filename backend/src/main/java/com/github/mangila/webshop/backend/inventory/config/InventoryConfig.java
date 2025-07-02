package com.github.mangila.webshop.backend.inventory.config;

import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.event.application.gateway.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.common.EventSubscriberProperties;
import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import com.github.mangila.webshop.backend.inventory.domain.event.InventoryEventTopicType;
import com.github.mangila.webshop.backend.inventory.domain.event.InventoryEventType;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.EnumSet;
import java.util.stream.Collectors;

@Configuration
public class InventoryConfig {

    private static final Logger log = LoggerFactory.getLogger(InventoryConfig.class);

    public static final EventSubscriberProperties INVENTORY_NEW_PRODUCT_PROPS = new EventSubscriberProperties("inventory-new-product-id", "PRODUCT", "PRODUCT_INSERTED");

    private final EventRegistryGateway eventRegistryGateway;
    private final EventServiceGateway eventServiceGateway;

    public InventoryConfig(EventRegistryGateway eventRegistryGateway,
                           EventServiceGateway eventServiceGateway) {
        this.eventRegistryGateway = eventRegistryGateway;
        this.eventServiceGateway = eventServiceGateway;
    }

    @PostConstruct
    public void registerInventoryEventTypes() {
        log.debug("Registering event types for inventory domain");
        EnumSet.allOf(InventoryEventType.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryEventType::name,
                        InventoryEventType::name))
                .forEach((key, value) -> {
                    log.info("Registering event type: {}", key);
                    eventRegistryGateway.eventTypeRegistry().register(key, value);
                });
    }

    @PostConstruct
    public void registerInventoryEventTopicTypes() {
        log.debug("Registering event topics for inventory domain");
        EnumSet.allOf(InventoryEventTopicType.class)
                .stream()
                .collect(Collectors.toMap(
                        InventoryEventTopicType::name,
                        InventoryEventTopicType::name))
                .forEach((key, value) -> {
                    log.info("Registering topic: {}", key);
                    eventRegistryGateway.eventTopicRegistry().register(key, value);
                });
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        EventSubscriber consumer = eventServiceGateway
                .subscriber()
                .save(INVENTORY_NEW_PRODUCT_PROPS.toCommand());
        log.info("Subscribed to {} on topic {} for event {}",
                consumer.getConsumer(),
                consumer.getTopic(),
                consumer.getType());
    }
}
