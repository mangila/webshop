package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;

import java.util.List;

public interface OutboxQueryRepository {

    List<Outbox> replay(OutboxReplayQuery query);

    List<OutboxMessage> findAllByPublished(OutboxPublished published, int limit);
}
