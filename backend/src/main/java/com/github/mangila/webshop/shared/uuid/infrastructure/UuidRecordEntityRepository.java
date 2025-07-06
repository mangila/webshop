package com.github.mangila.webshop.shared.uuid.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UuidRecordEntityRepository extends JpaRepository<UuidRecordEntity, UUID> {
}
