package com.github.mangila.webshop.product.model;

import com.github.mangila.webshop.shared.model.BaseEntity;

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
