package com.github.mangila.webshop.inventory.event;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryInboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryInboxEventListener.class);

    @EventListener(
            id = "inventory-product-created",
            condition = "#inboxEvent.event().value() == 'PRODUCT_CREATED'")
    @Transactional(propagation = Propagation.MANDATORY)
    void listenProductCreated(InboxEvent inboxEvent) {
        Ensure.equals("PRODUCT_CREATED", inboxEvent.event().value());
        log.info("Received inboxEvent: {}", inboxEvent);
    }

    @EventListener(
            id = "inventory-product-deleted",
            condition = "#inboxEvent.event().value() == 'PRODUCT_DELETED'")
    @Transactional(propagation = Propagation.MANDATORY)
    void listenProductDeleted(InboxEvent inboxEvent) {
        Ensure.equals("PRODUCT_DELETED", inboxEvent.event().value());
        log.info("Received inboxEvent: {}", inboxEvent);
    }
}
