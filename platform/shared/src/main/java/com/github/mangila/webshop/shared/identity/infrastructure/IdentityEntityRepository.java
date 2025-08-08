package com.github.mangila.webshop.shared.identity.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IdentityEntityRepository extends JpaRepository<IdentityEntity, UUID> {
}
