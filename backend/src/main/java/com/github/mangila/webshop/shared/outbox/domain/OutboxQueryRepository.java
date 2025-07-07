package com.github.mangila.webshop.shared.outbox.domain;


import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;

public interface OutboxQueryRepository {

    Outbox findById(OutboxId id) throws CqrsException;
}
