package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.exception.CqrsException;

import java.util.List;

public interface OutboxQueryRepository {

    Outbox findByIdOrThrow(OutboxId id) throws CqrsException;

    List<OutboxMessage> findAllByPublished(OutboxPublished published, int limit);
}
