package com.github.mangila.webshop.inventory.domain.event;

public enum InventoryEvent {
    INVENTORY_CREATE_NEW,
    UPDATED,
    DELETED,
    QUANTITY_INCREASED,
    QUANTITY_DECREASED,
    STOCK_DEPLETED,
    STOCK_REPLENISHED
}