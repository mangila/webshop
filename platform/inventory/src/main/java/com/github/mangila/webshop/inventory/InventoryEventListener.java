package com.github.mangila.webshop.inventory;

import com.github.mangila.webshop.shared.annotation.ObservedService;
import com.github.mangila.webshop.shared.event.DomainMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;

@ObservedService
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    @EventListener(
            value = DomainMessage.class,
            condition = "#root.args[0].event().value() == 'PRODUCT_CREATED'"
    )
    public void listen(DomainMessage message) {
        log.info("Received message: {}", message);
    }
}
