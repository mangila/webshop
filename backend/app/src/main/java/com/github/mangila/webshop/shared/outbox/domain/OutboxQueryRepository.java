package com.github.mangila.webshop.shared.outbox.domain;


import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;

import java.util.List;

public interface OutboxQueryRepository {

    Outbox findByIdOrThrow(OutboxId id) throws CqrsException;

    List<OutboxMessage> findAllByPublished(boolean published, int limit);
}
