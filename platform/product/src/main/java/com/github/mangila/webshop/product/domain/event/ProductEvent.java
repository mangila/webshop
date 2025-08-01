package com.github.mangila.webshop.product.domain.event;

import com.github.mangila.webshop.shared.annotation.EventTypes;

@EventTypes
public enum ProductEvent {
    PRODUCT_CREATED,
    PRODUCT_DELETED
}
