package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.util.Ensure;

public record ProductName(String value) {
    public ProductName {
        Ensure.notBlank(value,"Product name must not be blank");
    }
}
