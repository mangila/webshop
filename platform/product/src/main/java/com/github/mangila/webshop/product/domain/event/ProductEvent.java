package com.github.mangila.webshop.product.domain.event;

import com.github.mangila.webshop.shared.annotation.EventTypes;
import com.github.mangila.webshop.shared.model.Event;

@EventTypes
public enum ProductEvent {
    PRODUCT_CREATED,
    PRODUCT_DELETED;

    public Event asEvent() {
        return new Event(this.name());
    }
}
