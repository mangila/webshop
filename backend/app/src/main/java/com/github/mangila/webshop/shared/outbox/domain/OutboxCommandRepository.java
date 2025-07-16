package com.github.mangila.webshop.shared.outbox.domain;

import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;

public interface OutboxCommandRepository {

    Outbox insert(OutboxInsert command);

    void updateAsPublished(OutboxId id);
}
