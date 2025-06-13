package com.github.mangila.webshop.order.model;

import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.common.model.BaseEntity;

import java.util.Set;

public class OrderEntity extends BaseEntity {
    private String id;
    private String orderId;
    private String customerId;
    private String status;
    private String paymentMethod;
    private Set<Product> products;
}
