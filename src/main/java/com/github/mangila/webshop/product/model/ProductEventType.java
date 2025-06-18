package com.github.mangila.webshop.product.model;

public enum ProductEventType {
    PRODUCT_UPSERTED,
    PRODUCT_PRICE_UPDATED,
    PRODUCT_DELETED;

    public static ProductEventType from(ProductCommandType commandType) {
        return switch (commandType) {
            case UPSERT_PRODUCT -> PRODUCT_UPSERTED;
            case DELETE_PRODUCT -> PRODUCT_DELETED;
            case UPDATE_PRODUCT_PRICE -> PRODUCT_PRICE_UPDATED;
            case null -> throw new IllegalArgumentException("Command type cannot be null");
            default -> throw new IllegalArgumentException("Unknown command type: " + commandType);
        };
    }
}
