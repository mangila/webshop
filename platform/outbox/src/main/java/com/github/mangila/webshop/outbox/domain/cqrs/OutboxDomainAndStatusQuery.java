package com.github.mangila.webshop.outbox.domain.cqrs;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;

public record OutboxDomainAndStatusQuery(Domain domain, OutboxStatusType status, int limit
) {
    public OutboxDomainAndStatusQuery {
        Ensure.notNull(domain, Domain.class);
        Ensure.notNull(status, OutboxStatusType.class);
        Ensure.min(1, limit);
    }
}
