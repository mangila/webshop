package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.model.Domain;

import java.util.List;

public interface OutboxQueryRepository {

    List<Outbox> replay(OutboxReplayQuery query);

    List<OutboxProjection> findAllByDomainAndStatus(Domain domain, OutboxStatusType status, int limit);
}
