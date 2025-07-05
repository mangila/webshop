package com.github.mangila.webshop.shared.outbox.domain;

import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;

public interface OutboxCommandRepository {

    Outbox save(OutboxInsert command);

    void updateAsPublished(long id);
}
