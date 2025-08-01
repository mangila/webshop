package com.github.mangila.webshop.product.domain.primitive;


import com.github.mangila.webshop.shared.Ensure;

public record ProductName(String value) {
    public ProductName {
        Ensure.notBlank(value, ProductName.class);
    }
}
