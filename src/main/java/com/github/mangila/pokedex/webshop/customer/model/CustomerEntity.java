package com.github.mangila.pokedex.webshop.customer.model;

import com.github.mangila.pokedex.webshop.order.model.OrderEntity;

import java.util.Map;
import java.util.Set;

public class CustomerEntity {
    private String id;
    private String name;
    private Set<OrderEntity> orders;
    private Map<String, Object> extensions;
}
