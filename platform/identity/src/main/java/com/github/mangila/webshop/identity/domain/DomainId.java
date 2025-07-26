package com.github.mangila.webshop.identity.domain;


import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.util.Ensure;

import java.time.Instant;
import java.util.UUID;

public record DomainId(UUID id, Domain domain, Instant created) {

    public DomainId {
        Ensure.notNull(id, "Id must not be null");
        Ensure.notNull(domain, "Domain must not be null");
    }

    public static DomainId from(UUID id, Domain domain, Instant created) {
        return new DomainId(id, domain, created);
    }

    public static DomainId create(Domain domain) {
        return new DomainId(UUID.randomUUID(), domain, null);
    }
}
