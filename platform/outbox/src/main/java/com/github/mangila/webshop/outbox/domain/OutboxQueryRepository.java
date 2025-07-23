package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.exception.CqrsException;

import java.util.Optional;

public interface OutboxQueryRepository {

    Optional<Outbox> findById(OutboxId id);
}
