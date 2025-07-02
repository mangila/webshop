package com.github.mangila.webshop.backend.inventory.domain.event;

public enum InventoryEventType {
    CREATE_NEW,
    UPDATED,
    DELETED,
    QUANTITY_INCREASED,
    QUANTITY_DECREASED,
    STOCK_DEPLETED,
    STOCK_REPLENISHED
}