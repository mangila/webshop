package com.github.mangila.webshop.identity.infrastructure;

import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Domain;
import org.springframework.stereotype.Component;

@Component
public class DomainIdEntityMapper {

    private final RegistryService registryService;

    public DomainIdEntityMapper(RegistryService registryService) {
        this.registryService = registryService;
    }

    public DomainId toDomain(DomainIdEntity entity) {
        return DomainId.from(entity.getId(), new Domain(entity.getDomain(), registryService), entity.getCreated());
    }

    public DomainIdEntity toEntity(DomainId domainId) {
        return DomainIdEntity.from(domainId.id(), domainId.domain().value());
    }
}
