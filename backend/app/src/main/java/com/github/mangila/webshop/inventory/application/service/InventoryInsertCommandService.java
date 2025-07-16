package com.github.mangila.webshop.inventory.application.service;

import com.github.mangila.webshop.inventory.application.gateway.InventoryRepositoryGateway;
import com.github.mangila.webshop.inventory.domain.command.InventoryInsertCommand;
import com.github.mangila.webshop.inventory.domain.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryInsertCommandService {

    private final InventoryRepositoryGateway gateway;

    public InventoryInsertCommandService(InventoryRepositoryGateway gateway) {
        this.gateway = gateway;
    }

    public Inventory save(InventoryInsertCommand command) {
        return gateway.command().save(Inventory.from(command));
    }

    public List<Inventory> saveMany(List<InventoryInsertCommand> commands) {
        var inventories = commands.stream()
                .map(Inventory::from)
                .toList();
        return gateway.command().saveAll(inventories);
    }
}
