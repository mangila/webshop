package com.github.mangila.webshop.shared.outbox.application.gateway;

import com.github.mangila.webshop.shared.outbox.application.service.OutboxRegistryService;
import org.springframework.stereotype.Service;

@Service
public class OutboxRegistryGateway {

    private final OutboxRegistryService registry;

    public OutboxRegistryGateway(OutboxRegistryService registry) {
        this.registry = registry;
    }

    public OutboxRegistryService registry() {
        return registry;
    }
}
