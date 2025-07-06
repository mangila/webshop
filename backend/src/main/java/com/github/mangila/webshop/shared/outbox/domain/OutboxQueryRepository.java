package com.github.mangila.webshop.shared.outbox.domain;


import java.util.Optional;

public interface OutboxQueryRepository {

    Optional<Outbox> findById(OutboxId id);
}
