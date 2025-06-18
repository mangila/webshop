package com.github.mangila.webshop.product.model;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String id) {
        super(String.format("Product with id '%s' was not found", id));
    }
}
