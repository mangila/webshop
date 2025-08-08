package com.github.mangila.webshop.product.infrastructure.jpa;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product")
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
    private ProductStatusType status;

    @Column(nullable = false, updatable = false)
    private Instant created;

    @Column(nullable = false)
    private Instant updated;

    @Transient
    private volatile boolean isNew = true;

    protected ProductEntity() {
    }

    public ProductEntity(UUID id, String name, ObjectNode attributes, ProductStatusType status, Instant created, Instant updated) {
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.status = status;
        this.created = created;
        this.updated = updated;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public ProductStatusType getStatus() {
        return status;
    }

    public void setStatus(ProductStatusType status) {
        this.status = status;
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
