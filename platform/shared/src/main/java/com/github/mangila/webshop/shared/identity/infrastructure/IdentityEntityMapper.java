package com.github.mangila.webshop.shared.identity.infrastructure;

import com.github.mangila.webshop.shared.identity.domain.Identity;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import org.springframework.stereotype.Component;

@Component
public class IdentityEntityMapper {

    private final DomainRegistry domainRegistry;

    public IdentityEntityMapper(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    public Identity toDomain(IdentityEntity entity) {
        var domain = new Domain(entity.getDomain(), domainRegistry);
        return Identity.from(entity.getId(), domain, entity.getCreated());
    }

    public IdentityEntity toEntity(Identity identity) {
        return IdentityEntity.from(identity.id(), identity.domain().value(), identity.created());
    }
}
