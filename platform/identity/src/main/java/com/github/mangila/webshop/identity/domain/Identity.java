package com.github.mangila.webshop.identity.domain;


import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;

import java.time.Instant;
import java.util.UUID;

public record Identity(UUID id, Domain domain, Instant created) {

    public Identity {
        Ensure.notNull(id, UUID.class);
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(created, Instant.class);
    }

    public static Identity from(UUID id, Domain domain, Instant created) {
        return new Identity(id, domain, created);
    }

    public static Identity createNew(Domain domain) {
        final UUID id = UUID.randomUUID();
        final Instant now = Instant.now();
        return new Identity(id, domain, now);
    }
}
