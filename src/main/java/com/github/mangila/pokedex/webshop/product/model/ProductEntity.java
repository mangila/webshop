package com.github.mangila.pokedex.webshop.product.model;

import com.github.mangila.pokedex.webshop.shared.BaseEntity;

import java.math.BigDecimal;

public class ProductEntity extends BaseEntity {
    private String id;
    private String productId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
}
