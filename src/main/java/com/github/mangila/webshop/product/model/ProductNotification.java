package com.github.mangila.webshop.product.model;

import com.github.mangila.webshop.common.model.Notification;

public class ProductNotification extends Notification {

    public ProductNotification(Long eventId, String topic) {
        super(eventId, topic);
    }
}
