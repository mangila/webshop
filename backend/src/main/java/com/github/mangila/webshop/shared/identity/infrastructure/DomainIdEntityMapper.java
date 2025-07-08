package com.github.mangila.webshop.shared.identity.infrastructure;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.identity.domain.DomainId;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DomainIdEntityMapper {

    public DomainId toDomain(DomainIdEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to value")
        );
        return DomainId.from(entity.getId(), entity.getDomainKey(), entity.getIntent(), created);
    }

    public DomainIdEntity toEntity(DomainId domain) {
        return DomainIdEntity.from(domain.getId(), domain.getDomainKey(), domain.getIntent());
    }
}
