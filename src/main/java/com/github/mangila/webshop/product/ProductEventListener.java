package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.ProductNotification;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ProductEventListener {

    private final ProductService service;

    public ProductEventListener(ProductService service) {
        this.service = service;
    }

    @EventListener
    public void on(PayloadApplicationEvent<ProductNotification> notification) {

    }

}
