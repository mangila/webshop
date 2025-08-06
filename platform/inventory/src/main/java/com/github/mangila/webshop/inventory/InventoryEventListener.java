package com.github.mangila.webshop.inventory;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @EventListener(
            id = "inventory-listen-product-created",
            condition = "#inboxEvent.event().value() == 'PRODUCT_CREATED'"
    )
    void listenProductCreated(InboxEvent inboxEvent) {
        Ensure.equals("PRODUCT_CREATED", inboxEvent.event().value());
        log.info("Received inboxEvent: {}", inboxEvent);
    }

    @EventListener(
            id = "inventory-listen-product-deleted",
            condition = "#inboxEvent.event().value() == 'PRODUCT_DELETED'"
    )
    void listenProductDeleted(InboxEvent inboxEvent) {
        Ensure.equals("PRODUCT_DELETED", inboxEvent.event().value());
        log.info("Received inboxEvent: {}", inboxEvent);
    }
}
