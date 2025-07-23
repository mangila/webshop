package com.github.mangila.webshop.product.domain;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.domain.primitive.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductUnit;

import java.time.Instant;
import java.util.UUID;

public class Product {

    private ProductId id;
    private ProductName name;
    private ObjectNode attributes;
    private ProductUnit unit;
    private Instant created;
    private Instant updated;

    private Product(ProductId id, ProductName name, ObjectNode attributes, ProductUnit unit, Instant created, Instant updated) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.unit = unit;
        this.created = created;
        this.updated = updated;
    }

    public static Product from(UUID id, String name, ObjectNode attributes, ProductUnit unit, Instant created, Instant updated) {
        return new Product(new ProductId(id), new ProductName(name), attributes, unit, created, updated);
    }

    public ProductId getId() {
        return id;
    }

    public void setId(ProductId id) {
        this.id = id;
    }

    public ProductName getName() {
        return name;
    }

    public void setName(ProductName name) {
        this.name = name;
    }

    public ObjectNode getAttributes() {
        return attributes;
    }

    public void setAttributes(ObjectNode attributes) {
        this.attributes = attributes;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}
