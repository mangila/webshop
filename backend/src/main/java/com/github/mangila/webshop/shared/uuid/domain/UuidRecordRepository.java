package com.github.mangila.webshop.shared.uuid.domain;

import java.util.Optional;
import java.util.UUID;

public interface UuidRecordRepository {

    UuidRecord save(UuidRecord record);

    Optional<UuidRecord> findById(UUID id);

    boolean existsById(UUID id);
}
