package com.github.mangila.webshop.identity.infrastructure;

import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class DomainIdEntityMapper {

    private final RegistryService registryService;

    public DomainIdEntityMapper(RegistryService registryService) {
        this.registryService = registryService;
    }

    public DomainId toDomain(DomainIdEntity entity) {
        Instant created = entity.getCreated().orElseThrow(
                () -> new ApplicationException("Created date is required to map from entity to value")
        );
        return DomainId.from(entity.getId(), Domain.from(entity.getDomain(), registryService), entity.getIntent(), created);
    }

    public DomainIdEntity toEntity(DomainId domain) {
        return DomainIdEntity.from(domain.getId(), domain.getDomain().value(), domain.getIntent());
    }
}
