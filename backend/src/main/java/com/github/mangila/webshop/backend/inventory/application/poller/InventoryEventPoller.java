package com.github.mangila.webshop.backend.inventory.application.poller;

import com.github.mangila.webshop.backend.event.application.gateway.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import com.github.mangila.webshop.backend.event.domain.query.EventFindByTopicAndTypeAndOffsetQuery;
import com.github.mangila.webshop.backend.event.domain.query.EventSubscriberByIdQuery;
import com.github.mangila.webshop.backend.inventory.application.gateway.InventoryServiceGateway;
import com.github.mangila.webshop.backend.inventory.config.InventoryConfig;
import com.github.mangila.webshop.backend.inventory.domain.command.InventoryInsertCommand;
import com.github.mangila.webshop.backend.inventory.domain.model.InventoryId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InventoryEventPoller {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventPoller.class);

    private final InventoryServiceGateway inventoryServiceGateway;
    private final EventServiceGateway eventServiceGateway;

    public InventoryEventPoller(InventoryServiceGateway inventoryServiceGateway,
                                EventServiceGateway eventServiceGateway) {
        this.inventoryServiceGateway = inventoryServiceGateway;
        this.eventServiceGateway = eventServiceGateway;
    }

    @Transactional
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void pollForNewProductInsertedEvents() {
        EventSubscriber subscriber = eventServiceGateway.subscriber()
                .findById(new EventSubscriberByIdQuery(InventoryConfig.INVENTORY_NEW_PRODUCT_PROPS.consumer()))
                .orElseThrow();
        List<Event> pendingEvents = eventServiceGateway.subscriber()
                .findEventsByTopicAndTypeAndOffset(new EventFindByTopicAndTypeAndOffsetQuery(
                        subscriber.getTopic(),
                        subscriber.getType(),
                        subscriber.getLatestOffset(),
                        10
                ));
        if (CollectionUtils.isEmpty(pendingEvents)) {
            log.debug("No events found for consumer {}", subscriber.getConsumer());
            return;
        }
        log.debug("Found {} events for consumer {}", pendingEvents.size(), subscriber.getConsumer());
        var inventories = pendingEvents.stream()
                .peek(event -> log.debug("Processing event {}", event))
                .map(Event::getAggregateId)
                .map(InventoryId::new)
                .map(InventoryInsertCommand::from)
                .toList();
        inventoryServiceGateway.insert().saveMany(inventories);
        eventServiceGateway.subscriber().acknowledge(subscriber, pendingEvents);
    }


}
