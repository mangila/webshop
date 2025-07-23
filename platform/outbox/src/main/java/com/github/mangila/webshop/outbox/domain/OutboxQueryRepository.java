package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.exception.CqrsException;

public interface OutboxQueryRepository {

    Outbox findByIdOrThrow(OutboxId id) throws CqrsException;
}
