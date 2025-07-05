package com.github.mangila.webshop.shared.domain.outbox.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEntityCommandRepository extends JpaRepository<OutboxEntity, Long> {
}
