package com.github.mangila.webshop.shared.outbox.domain;

import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxFindById;

import java.util.Optional;

public interface OutboxQueryRepository {

    Optional<Outbox> findById(OutboxFindById query);
}
