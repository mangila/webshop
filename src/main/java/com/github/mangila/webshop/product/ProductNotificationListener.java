package com.github.mangila.webshop.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.EventService;
import com.github.mangila.webshop.common.PgNotificationListener;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import com.github.mangila.webshop.product.model.ProductNotification;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProductNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(ProductNotificationListener.class);

    private final ObjectMapper objectMapper;
    private final PgNotificationListener pgNotificationListener;
    private final EventService eventService;
    private final ProductCommandService productCommandService;

    public ProductNotificationListener(ObjectMapper objectMapper,
                                       @Qualifier("productPgNotificationListener") PgNotificationListener pgNotificationListener, EventService eventService,
                                       ProductCommandService productCommandService) {
        this.objectMapper = objectMapper;
        this.pgNotificationListener = pgNotificationListener;
        this.eventService = eventService;
        this.productCommandService = productCommandService;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        pgNotificationListener.start();
    }

    @Async
    @EventListener
    public void onNotification(ProductNotification notification) {
        try {
            log.info("Received notification: {} -- {}", notification.getId(), notification.getTopic());
            var event = eventService.acknowledgeEvent(notification.getId(), notification.getTopic());
            var productId = event.getAggregateId();
            var eventType = ProductEventType.valueOf(event.getEventType());
            switch (eventType) {
                case PRICE_CHANGED, EXTENSION_CHANGED, QUANTITY_CHANGED -> {
                    log.info("Ignoring -- not yet supported event: {}", event.getEventType());
                }
                case CREATE_NEW -> {
                    log.info("Creating new product: {}", event.getEventData());
                    var product = objectMapper.readValue(event.getEventData(), Product.class);
                    productCommandService.createNewProduct(product);
                }
                case DELETE -> {
                    log.info("Deleting product: {}", productId);
                    productCommandService.deleteProductById(productId);
                }
                case null -> throw new IllegalArgumentException(String.format("Invalid eventType: %s", eventType));
            }
        } catch (Exception e) {
            handleException(e, notification);
        }
    }

    public void handleException(Exception e, ProductNotification notification) {
        log.error("Failed to process notification: {}", notification, e);
    }

    @PreDestroy
    public void shutdownPgListener() {
        pgNotificationListener.shutdown();
    }
}
