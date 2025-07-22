package com.github.mangila.webshop.inventory;

import com.github.mangila.webshop.shared.event.DomainMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @Async
    @TransactionalEventListener(
            condition = "#message.event().value() == 'PRODUCT_CREATED'",
            fallbackExecution = true
    )
    void listen(DomainMessage message) {
        log.info("Received message: {}", message);
    }

}
