package com.github.mangila.webshop.shared.domain.uuid.infrastructure;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.domain.uuid.domain.UuidRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UuidRecordEntityMapper {

    public UuidRecord toDomain(UuidRecordEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to domain")
        );
        return UuidRecord.from(entity.getId(), entity.getIntent(), created);
    }

    public UuidRecordEntity toEntity(UuidRecord domain) {
        return UuidRecordEntity.from(domain.getId(), domain.getIntent());
    }
}
