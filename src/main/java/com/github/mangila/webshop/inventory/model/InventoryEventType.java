package com.github.mangila.webshop.inventory.model;

/**
 * Enum representing the types of events that can occur for Inventory entities.
 */
public enum InventoryEventType {
    CREATE_NEW,
    UPDATED,
    DELETED,
    QUANTITY_INCREASED,
    QUANTITY_DECREASED,
    STOCK_DEPLETED,
    STOCK_REPLENISHED
}