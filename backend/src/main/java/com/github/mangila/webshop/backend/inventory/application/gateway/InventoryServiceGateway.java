package com.github.mangila.webshop.backend.inventory.application.gateway;

import com.github.mangila.webshop.backend.inventory.application.service.InventoryInsertCommandService;
import com.github.mangila.webshop.backend.inventory.application.service.InventoryQueryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceGateway {

    private final InventoryInsertCommandService command;
    private final InventoryQueryService query;

    public InventoryServiceGateway(InventoryInsertCommandService command, InventoryQueryService query) {
        this.command = command;
        this.query = query;
    }

    public InventoryInsertCommandService command() {
        return command;
    }

    public InventoryQueryService query() {
        return query;
    }
}
