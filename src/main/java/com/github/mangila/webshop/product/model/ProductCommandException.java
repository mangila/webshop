package com.github.mangila.webshop.product.model;

import com.github.mangila.webshop.product.command.model.ProductCommandType;

public class ProductCommandException extends RuntimeException {

    public ProductCommandException(ProductCommandType commandType, String message) {
        super(String.format("Failed Command: %s -- %s", commandType, message));
    }
}
