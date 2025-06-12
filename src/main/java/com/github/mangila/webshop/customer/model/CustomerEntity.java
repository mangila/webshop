package com.github.mangila.webshop.customer.model;

import com.github.mangila.webshop.order.model.OrderEntity;
import com.github.mangila.webshop.common.model.BaseEntity;

import java.util.Set;

public class CustomerEntity extends BaseEntity {
    private String id;
    private String customerId;
    private String name;
    private Set<OrderEntity> orders;
}
