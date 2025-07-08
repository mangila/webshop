package com.github.mangila.webshop.shared.uuid.domain;

import java.util.UUID;

public interface UuidRecordRepository {

    UuidRecord save(UuidRecord record);

    UuidRecord findById(UUID id);

    boolean existsById(UUID id);
}
