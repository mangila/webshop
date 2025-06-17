package com.github.mangila.webshop.product;

import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommandType;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductValidator {

    public void validateByCommand(ProductCommandType command, Product product) {
        ensureProduct(product);
        switch (command) {
            case UPSERT_PRODUCT -> {
                ensureProductId(product);
                ensureProductName(product);
                ensureProductPrice(product);
            }
            case DELETE_PRODUCT -> ensureProductId(product);
            case UPDATE_PRODUCT_PRICE -> ensureProductPrice(product);
            case null -> throw new IllegalStateException("command must not be null");
            default -> throw new IllegalStateException("command not supported:");
        }
    }

    private static void ensureProductPrice(Product product) {
        Objects.requireNonNull(product.getPrice(), "price must not be null");
    }

    private static void ensureProductName(Product product) {
        Objects.requireNonNull(product.getName(), "name must not be null");
    }

    private static void ensureProductId(Product product) {
        Objects.requireNonNull(product.getId(), "id must not be null");
    }

    private static void ensureProduct(Product product) {
        Objects.requireNonNull(product, "product must not be null");
    }
}

