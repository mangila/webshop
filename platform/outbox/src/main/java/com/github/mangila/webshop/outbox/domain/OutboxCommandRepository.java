package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.command.*;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.List;
import java.util.Optional;

public interface OutboxCommandRepository {

    Outbox insert(CreateOutboxCommand command);

    Optional<Outbox> findForUpdate(FindOutboxForUpdateCommand command);

    void updateStatus(UpdateOutboxStatusCommand command);

    Optional<OutboxSequence> findCurrentSequence(OutboxAggregateId aggregateId);

    void updateSequence(UpdateOutboxSequenceCommand outboxSequence);

    void deleteByIds(List<OutboxId> ids);

    void delete(DeleteOutboxCommand command);
}
