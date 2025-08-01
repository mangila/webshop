package com.github.mangila.webshop.inventory;

import com.github.mangila.webshop.shared.model.DomainMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @EventListener(
            value = DomainMessage.class,
            condition = "#message.event().value() == 'PRODUCT_CREATED'"
    )
    void listenProductCreated(DomainMessage message) {
        log.info("Received message: {}", message);
    }

    @EventListener(
            value = DomainMessage.class,
            condition = "#message.event().value() == 'PRODUCT_DELETED'"
    )
    void listenProductDeleted(DomainMessage message) {
        log.info("Received message: {}", message);
    }
}
