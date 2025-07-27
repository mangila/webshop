package com.github.mangila.webshop.identity.infrastructure;

import com.github.mangila.webshop.identity.domain.DomainId;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.springframework.stereotype.Component;

@Component
public class DomainIdEntityMapper {

    private final DomainRegistry domainRegistry;

    public DomainIdEntityMapper(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    public DomainId toDomain(DomainIdEntity entity) {
        var domain = new Domain(entity.getDomain(), domainRegistry);
        return DomainId.from(entity.getId(), domain, entity.getCreated());
    }

    public DomainIdEntity toEntity(DomainId domainId) {
        return DomainIdEntity.from(domainId.id(), domainId.domain().value());
    }
}
