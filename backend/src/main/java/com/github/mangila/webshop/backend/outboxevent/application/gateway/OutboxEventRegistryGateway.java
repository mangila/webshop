package com.github.mangila.webshop.backend.outboxevent.application.gateway;

import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventCommandService;
import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventQueryService;
import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventRegistryService;
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
