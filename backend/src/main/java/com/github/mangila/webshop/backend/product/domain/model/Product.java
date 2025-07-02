package com.github.mangila.webshop.backend.product.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", nullable = false))
    private ProductId id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "name", nullable = false))
    private ProductName name;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "price", nullable = false))
    private ProductPrice price;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;

    @Type(JsonBinaryType.class)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
    private JsonNode attributes;

    protected Product() {
    }

    private Product(ProductId id, ProductName name, ProductPrice price, Instant created, Instant updated, JsonNode attributes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.created = created;
        this.updated = updated;
        this.attributes = attributes;
    }

    private Product(ProductId id, ProductName name, ProductPrice price, JsonNode attributes) {
        this(id, name, price, null, null, attributes);
    }

    public static Product from(UUID uuid, ProductInsertCommand command) {
        return new Product(
                new ProductId(uuid),
                command.name(),
                command.price(),
                command.attributes()
        );
    }

    public JsonNode toJsonNode(JsonMapper jsonMapper) {
        return jsonMapper.toJsonNode(this);
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

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
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

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }
}

