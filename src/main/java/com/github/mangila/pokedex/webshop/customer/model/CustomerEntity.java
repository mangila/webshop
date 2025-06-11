package com.github.mangila.pokedex.webshop.customer.model;

import com.github.mangila.pokedex.webshop.order.model.OrderEntity;
import com.github.mangila.pokedex.webshop.shared.BaseEntity;

import java.util.Set;

public class CustomerEntity extends BaseEntity {
    private String id;
    private String name;
    private Set<OrderEntity> orders;
}
