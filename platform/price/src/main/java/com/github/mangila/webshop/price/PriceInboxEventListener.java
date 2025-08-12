package com.github.mangila.webshop.price;

import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriceInboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(PriceInboxEventListener.class);

    @EventListener(
            id = "price-product-created",
            condition = "#inboxEvent.event().value() == 'PRODUCT_CREATED'")
    @Transactional(propagation = Propagation.MANDATORY)
    void listenProductCreated(InboxEvent inboxEvent) {
        log.info("Received inboxEvent: {}", inboxEvent);
    }

    @EventListener(
            id = "price-product-deleted",
            condition = "#inboxEvent.event().value() == 'PRODUCT_DELETED'")
    @Transactional(propagation = Propagation.MANDATORY)
    void listenProductDeleted(InboxEvent inboxEvent) {
        log.info("Received inboxEvent: {}", inboxEvent);
    }
}
