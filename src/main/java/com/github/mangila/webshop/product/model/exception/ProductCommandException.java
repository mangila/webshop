package com.github.mangila.webshop.product.model.exception;

import com.github.mangila.webshop.product.model.ProductCommandType;

public class ProductCommandException extends RuntimeException {

    public ProductCommandException(ProductCommandType commandType, String message) {
        super(String.format("Failed Command: %s -- %s", commandType, message));
    }
}
