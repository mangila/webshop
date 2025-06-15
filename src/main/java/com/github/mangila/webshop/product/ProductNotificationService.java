package com.github.mangila.webshop.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.common.notification.model.ProductNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ProductNotificationService {

    private static final Logger log = LoggerFactory.getLogger(ProductNotificationService.class);

    private final ProductEventService productEventService;

    public ProductNotificationService(ProductEventService productEventService) {
        this.productEventService = productEventService;
    }

    @Async
    @EventListener
    public void onNotification(ProductNotification notification) {
        log.debug("Received ProductNotification: {} -- {}", notification.getTopic(), notification.getId());
        try {
            productEventService.acknowledgeEvent(notification.getId());
        } catch (JsonProcessingException e) {
            log.error("Failed to process notification: {}", notification, e);
        }
    }

}
