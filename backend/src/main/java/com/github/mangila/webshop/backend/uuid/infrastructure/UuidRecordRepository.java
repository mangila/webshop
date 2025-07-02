package com.github.mangila.webshop.backend.uuid.infrastructure;

import com.github.mangila.webshop.backend.uuid.domain.UuidRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UuidRecordRepository extends JpaRepository<UuidRecord, UUID> {
}
