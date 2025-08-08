package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxUpdateStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.List;
import java.util.Optional;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    Optional<Outbox> findByIdWhereStatusNotPublishedForUpdate(OutboxId id);

    void updateStatus(OutboxUpdateStatusCommand command);

    Optional<OutboxSequence> findAndLockByAggregateId(OutboxAggregateId aggregateId);

    void updateSequence(OutboxSequence outboxSequence);

    void deleteByIds(List<OutboxId> ids);
}
