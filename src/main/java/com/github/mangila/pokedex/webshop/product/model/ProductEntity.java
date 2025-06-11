package com.github.mangila.pokedex.webshop.product.model;

import java.math.BigDecimal;
import java.util.Map;

public class ProductEntity {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String category;
    private Map<String, Object> extensions;
}
