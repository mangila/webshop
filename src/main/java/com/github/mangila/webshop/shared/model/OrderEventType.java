package com.github.mangila.webshop.shared.model;

/**
 * Enum representing the types of events that can occur for Order entities.
 */
public enum OrderEventType {
    CREATED,
    UPDATED,
    DELETED,
    STATUS_CHANGED,
    PAYMENT_METHOD_CHANGED,
    PRODUCT_ADDED,
    PRODUCT_REMOVED
}