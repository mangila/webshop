package com.github.mangila.webshop.backend.inventory.application.gateway;

import com.github.mangila.webshop.backend.inventory.infrastructure.InventoryCommandRepository;
import com.github.mangila.webshop.backend.inventory.infrastructure.InventoryQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryRepositoryGateway {

    private final InventoryCommandRepository command;
    private final InventoryQueryRepository query;

    public InventoryRepositoryGateway(InventoryCommandRepository command, InventoryQueryRepository query) {
        this.command = command;
        this.query = query;
    }

    public InventoryCommandRepository command() {
        return command;
    }

    public InventoryQueryRepository query() {
        return query;
    }
}
