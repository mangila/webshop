package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;

import java.util.List;
import java.util.Optional;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    Optional<OutboxMessage> findByIdAndPublishedForUpdate(OutboxId id, OutboxPublished published);

    void updatePublished(OutboxId id, OutboxPublished published);

    List<OutboxMessage> findAllByPublishedForUpdate(OutboxPublished published, int limit);
}
