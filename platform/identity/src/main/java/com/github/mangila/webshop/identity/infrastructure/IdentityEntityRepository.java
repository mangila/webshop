package com.github.mangila.webshop.identity.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdentityEntityRepository extends JpaRepository<IdentityEntity, UUID> {
}
