package com.github.mangila.webshop.product.domain.event;

import com.github.mangila.webshop.shared.annotation.DomainEvent;
import com.github.mangila.webshop.shared.model.Event;

@DomainEvent
public enum ProductEvent {
    PRODUCT_CREATED,
    PRODUCT_DELETED,
    PRODUCT_STATUS_UPDATED;

    public Event toEvent() {
        return new Event(this.name());
    }
}
