package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.List;

public interface OutboxQueryRepository {

    List<OutboxId> findAllIdsByStatus(FindAllOutboxIdByStatusQuery query);
}
