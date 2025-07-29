package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;

import java.util.Optional;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    Optional<OutboxMessage> findByIdAndStatusForUpdate(OutboxId id, OutboxStatusType status);

    void updateStatus(OutboxId id, OutboxStatusType status, OutboxUpdated updated);

    Optional<OutboxSequence> findCurrentSequenceAndLockByAggregateId(OutboxAggregateId aggregateId);

    void updateSequence(OutboxSequence outboxSequence);
}
