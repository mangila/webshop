package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublishedAt;

import java.util.Optional;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    Optional<OutboxMessage> findByIdAndPublishedForUpdate(OutboxId id, OutboxPublished published);

    void updatePublished(OutboxId id, OutboxPublished published, OutboxPublishedAt now);
    
    Optional<OutboxSequence> findSequenceAndLockByAggregateId(OutboxAggregateId aggregateId);

    void updateNewSequence(OutboxSequence outboxSequence);
}
