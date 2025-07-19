package com.github.mangila.webshop.outbox.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {
}
