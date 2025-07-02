package com.github.mangila.webshop.backend.inventory.application.gateway;

import com.github.mangila.webshop.backend.inventory.application.service.InventoryInsertCommandService;
import com.github.mangila.webshop.backend.inventory.application.service.InventoryQueryService;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceGateway {

    private final InventoryInsertCommandService insert;
    private final InventoryQueryService query;

    public InventoryServiceGateway(InventoryInsertCommandService insert, InventoryQueryService query) {
        this.insert = insert;
        this.query = query;
    }

    public InventoryInsertCommandService insert() {
        return insert;
    }

    public InventoryQueryService query() {
        return query;
    }
}
