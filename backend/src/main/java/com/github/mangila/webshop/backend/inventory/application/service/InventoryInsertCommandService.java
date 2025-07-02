package com.github.mangila.webshop.backend.inventory.application.service;

import com.github.mangila.webshop.backend.inventory.application.gateway.InventoryRepositoryGateway;
import com.github.mangila.webshop.backend.inventory.domain.command.InventoryInsertCommand;
import com.github.mangila.webshop.backend.inventory.domain.model.Inventory;
import org.springframework.stereotype.Service;

@Service
public class InventoryInsertCommandService {

    private final InventoryRepositoryGateway gateway;

    public InventoryInsertCommandService(InventoryRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    public Inventory execute(InventoryInsertCommand command) {
        return gateway.command().save(Inventory.from(command));
    }
}
