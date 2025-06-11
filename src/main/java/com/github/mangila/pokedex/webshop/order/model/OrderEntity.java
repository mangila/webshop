package com.github.mangila.pokedex.webshop.order.model;

import com.github.mangila.pokedex.webshop.product.model.ProductEntity;
import com.github.mangila.pokedex.webshop.shared.BaseEntity;

import java.util.Set;

public class OrderEntity extends BaseEntity {
    private String id;
    private String customerId;
    private Set<ProductEntity> products;
}
