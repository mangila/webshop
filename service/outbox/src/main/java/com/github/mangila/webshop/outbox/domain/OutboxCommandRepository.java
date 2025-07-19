package com.github.mangila.webshop.outbox.domain;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsert;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsert command);

    void updateAsPublished(OutboxId id);
}
