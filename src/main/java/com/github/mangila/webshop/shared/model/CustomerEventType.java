package com.github.mangila.webshop.shared.model;

/**
 * Enum representing the types of events that can occur for Customer entities.
 */
public enum CustomerEventType {
    CREATED,
    UPDATED,
    DELETED,
    NAME_CHANGED,
    ORDER_ADDED,
    ORDER_REMOVED
}