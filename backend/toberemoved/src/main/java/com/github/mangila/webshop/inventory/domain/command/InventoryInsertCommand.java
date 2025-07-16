package com.github.mangila.webshop.inventory.domain.command;

import com.github.mangila.webshop.inventory.domain.model.InventoryId;
import com.github.mangila.webshop.inventory.domain.model.InventoryQuantity;

public record InventoryInsertCommand(InventoryId id, InventoryQuantity quantity) {

    public static InventoryInsertCommand from(InventoryId id) {
        return new InventoryInsertCommand(id, InventoryQuantity.DEFAULT);
    }
}
