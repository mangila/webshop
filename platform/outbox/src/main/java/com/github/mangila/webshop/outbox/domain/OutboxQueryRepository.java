package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.List;
import java.util.Optional;

public interface OutboxQueryRepository {

    List<Outbox> replay(OutboxReplayQuery query);
}
