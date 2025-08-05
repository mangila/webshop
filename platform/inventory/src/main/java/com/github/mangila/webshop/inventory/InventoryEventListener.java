package com.github.mangila.webshop.inventory;

import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @EventListener(
            value = InboxEvent.class,
            condition = "#message.event().value() == 'PRODUCT_CREATED'"
    )
    void listenProductCreated(InboxEvent message) {
        log.info("Received message: {}", message);
    }

    @EventListener(
            value = InboxEvent.class,
            condition = "#message.event().value() == 'PRODUCT_DELETED'"
    )
    void listenProductDeleted(InboxEvent message) {
        log.info("Received message: {}", message);
    }
}
