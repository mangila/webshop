package com.github.mangila.webshop.outboxevent.application.gateway;

import com.github.mangila.webshop.outboxevent.application.service.OutboxEventRegistryService;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventRegistryGateway {

    private final OutboxEventRegistryService registry;

    public OutboxEventRegistryGateway(OutboxEventRegistryService registry) {
        this.registry = registry;
    }

    public OutboxEventRegistryService registry() {
        return registry;
    }
}
