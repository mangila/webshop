package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.OutboxDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;

import java.util.List;

public interface OutboxQueryRepository {

    List<Outbox> replay(OutboxReplayQuery query);

    List<OutboxProjection> findAllByDomainAndStatus(OutboxDomainAndStatusQuery query);

    List<OutboxId> findAllIdsByStatusAndDateBefore(OutboxStatusAndDateBeforeQuery query);
}
