package com.github.mangila.webshop.delivery.model;

/**
 * Enum representing the types of events that can occur for Delivery entities.
 */
public enum DeliveryEventType {
    CREATED,
    UPDATED,
    DELETED,
    SCHEDULED,
    DISPATCHED,
    IN_TRANSIT,
    DELIVERED,
    FAILED,
    RETURNED
}