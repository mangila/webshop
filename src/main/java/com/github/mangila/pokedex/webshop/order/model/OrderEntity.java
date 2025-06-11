package com.github.mangila.pokedex.webshop.order.model;

import com.github.mangila.pokedex.webshop.product.model.ProductEntity;

import java.util.Map;
import java.util.Set;

public class OrderEntity {
    private String id;
    private String customerId;
    private Set<ProductEntity> products;
    private Map<String, Object> extensions;
}
