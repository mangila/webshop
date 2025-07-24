package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;

import java.util.Optional;

public interface OutboxQueryRepository {

    Optional<Outbox> findById(OutboxId id);
}
