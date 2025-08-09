package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdsByStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.List;

public interface OutboxQueryRepository {

    List<Outbox> findAllByDomainAndStatus(FindAllOutboxByDomainAndStatusQuery query);

    List<OutboxId> findAllIdsByStatusAndDateBefore(FindAllOutboxIdsByStatusAndDateBeforeQuery query);
}
