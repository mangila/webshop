package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.exception.CqrsException;

import java.util.List;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    OutboxMessage findByIdForUpdateOrThrow(OutboxId id) throws CqrsException;

    void updateAsPublished(OutboxId id, OutboxPublished published);

    List<OutboxMessage> findAllByPublishedForUpdate(OutboxPublished published, int limit);
}
