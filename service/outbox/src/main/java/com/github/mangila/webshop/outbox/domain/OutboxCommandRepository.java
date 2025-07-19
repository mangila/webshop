package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsertCommand command);

    void updateAsPublished(OutboxId id);
}
