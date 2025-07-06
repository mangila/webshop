package com.github.mangila.webshop.shared.outbox.domain;


import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;

import java.util.Optional;

public interface OutboxQueryRepository {

    Optional<Outbox> findById(OutboxId id);
}
