package com.github.mangila.webshop.inventory.model;

import java.math.BigInteger;
import java.time.Instant;

public class InventoryEntity {

    private String id;
    private String productId;
    private BigInteger quantity;
    private Instant updated;
    private String extensions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}