package com.github.mangila.webshop.backend.product.model;

import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.model.EventType;

public enum ProductEventType implements EventType {

    PRODUCT_UPSERTED,
    PRODUCT_PRICE_UPDATED,
    PRODUCT_DELETED;

    @Override
    public EventTopic topic() {
        return EventTopic.PRODUCT;
    }

    @Override
    public String type() {
        return name();
    }
}
