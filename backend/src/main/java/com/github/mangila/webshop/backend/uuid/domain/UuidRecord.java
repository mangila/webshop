package com.github.mangila.webshop.backend.uuid.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class UuidRecord {
    @Id
    private UUID uuid;

}
