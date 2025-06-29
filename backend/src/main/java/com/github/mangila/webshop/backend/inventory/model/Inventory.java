package com.github.mangila.webshop.backend.inventory.model;

import java.math.BigInteger;
import java.time.Instant;

public class Inventory {

    private String productId;
    private BigInteger quantity;
    private Instant updated;
    private String extensions;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public BigInteger getQuantity() {
        return quantity;
    }

    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", updated=" + updated +
                ", extensions='" + extensions + '\'' +
                '}';
    }
}