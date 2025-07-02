package com.github.mangila.webshop.backend.inventory.application.poller;

import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.application.gateway.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.command.EventPublishCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import com.github.mangila.webshop.backend.event.domain.query.EventFindByTopicAndTypeAndOffsetQuery;
import com.github.mangila.webshop.backend.event.domain.query.EventSubscriberByIdQuery;
import com.github.mangila.webshop.backend.inventory.application.gateway.InventoryServiceGateway;
import com.github.mangila.webshop.backend.inventory.config.InventoryConfig;
import com.github.mangila.webshop.backend.inventory.domain.command.InventoryInsertCommand;
import com.github.mangila.webshop.backend.inventory.domain.event.InventoryEventTopicType;
import com.github.mangila.webshop.backend.inventory.domain.event.InventoryEventType;
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

    private final JsonMapper jsonMapper;
    private final InventoryServiceGateway inventoryServiceGateway;
    private final EventServiceGateway eventServiceGateway;

    public InventoryEventPoller(JsonMapper jsonMapper,
                                InventoryServiceGateway inventoryServiceGateway,
                                EventServiceGateway eventServiceGateway) {
        this.jsonMapper = jsonMapper;
        this.inventoryServiceGateway = inventoryServiceGateway;
        this.eventServiceGateway = eventServiceGateway;
    }

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    @Transactional
    public void pollForNewProducts() {
        var consumerId = InventoryConfig.INVENTORY_NEW_PRODUCT_PROPS.consumer();
        EventSubscriber subscriber = eventServiceGateway.subscriber()
                .findById(new EventSubscriberByIdQuery(consumerId))
                .orElseThrow(() -> new ApiException(String.format("No subscriber with id: '%s'", consumerId), Event.class));
        List<Event> pendingEvents = eventServiceGateway.subscriber()
                .findEventsByTopicAndTypeAndOffset(EventFindByTopicAndTypeAndOffsetQuery.from(
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
        var inventoryInsertCommands = pendingEvents.stream()
                .peek(event -> log.debug("Processing pending event {}", event))
                .map(Event::getAggregateId)
                .map(InventoryId::new)
                .map(InventoryInsertCommand::from)
                .toList();
        var eventPublishCommands = inventoryServiceGateway.insert()
                .saveMany(inventoryInsertCommands)
                .stream()
                .peek(inventory -> log.debug("Saved inventory {}", inventory))
                .map(inventory -> EventPublishCommand.from(InventoryEventTopicType.INVENTORY.name(),
                        InventoryEventType.INVENTORY_CREATE_NEW.name(),
                        inventory.getId().value(),
                        jsonMapper.toJsonNode(inventory)))
                .peek(command -> log.debug("Created EventPublishCommand {}", command))
                .toList();
        eventServiceGateway.publisher()
                .publishMany(eventPublishCommands)
                .stream()
                .peek(event -> log.debug("Published event {}", event))
                .toList();
        eventServiceGateway.subscriber().acknowledge(subscriber, pendingEvents);
    }
}
