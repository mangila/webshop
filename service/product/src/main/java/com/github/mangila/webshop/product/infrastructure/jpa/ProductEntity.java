package com.github.mangila.webshop.product.infrastructure.jpa;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity implements Persistable<UUID> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Type(JsonBinaryType.class)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
    private ObjectNode attributes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductUnit unit;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;

    @Transient
    private volatile boolean isNew = true;

    protected ProductEntity() {
    }

    private ProductEntity(UUID id, String name, ObjectNode attributes, ProductUnit unit) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.unit = unit;
    }

    public static ProductEntity from(UUID id, String name, ObjectNode attributes, ProductUnit unit) {
        return new ProductEntity(id, name, attributes, unit);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return isNew;
    }

    @Transient
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
