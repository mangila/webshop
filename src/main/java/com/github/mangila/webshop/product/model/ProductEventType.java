package com.github.mangila.webshop.product.model;

/**
 * Enum representing the types of events that can occur for Product entities.
 */
public enum ProductEventType {
    CREATE_NEW,
    DELETE,
    PRICE_CHANGED,
    QUANTITY_CHANGED,
    EXTENSION_CHANGED
}