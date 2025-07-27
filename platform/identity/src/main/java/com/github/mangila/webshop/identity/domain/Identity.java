package com.github.mangila.webshop.identity.domain;


import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;

import java.time.Instant;
import java.util.UUID;

public record Identity(UUID id, Domain domain, Instant created) {

    public Identity {
        Ensure.notNull(id, "Identity Id must not be null");
        Ensure.notNull(domain, "Identity Domain must not be null");
        Ensure.notNull(created, "Identity Created must not be null");
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
