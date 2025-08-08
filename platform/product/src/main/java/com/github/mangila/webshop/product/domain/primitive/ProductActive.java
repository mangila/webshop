package com.github.mangila.webshop.product.domain.primitive;

public record ProductActive(boolean value) {

    private static final ProductActive ACTIVE = new ProductActive(true);
    private static final ProductActive INACTIVE = new ProductActive(false);

    public static ProductActive active() {
        return ACTIVE;
    }

    public static ProductActive inactive() {
        return INACTIVE;
    }
}
