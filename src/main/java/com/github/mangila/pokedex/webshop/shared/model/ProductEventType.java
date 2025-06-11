package com.github.mangila.pokedex.webshop.shared.model;

/**
 * Enum representing the types of events that can occur for Product entities.
 */
public enum ProductEventType {
    CREATED,
    UPDATED,
    DELETED,
    PRICE_CHANGED,
    CATEGORY_CHANGED,
    DESCRIPTION_UPDATED,
    IMAGE_UPDATED
}