package com.github.mangila.webshop.product.model;

/**
 * Enum representing the types of events that can occur for Product entities.
 */
public enum ProductEventType {
    CREATE_NEW,
    UPDATED,
    DELETED,
    PRICE_CHANGED,
    CATEGORY_CHANGED,
    DESCRIPTION_UPDATED,
    IMAGE_UPDATED
}