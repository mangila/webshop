package com.github.mangila.webshop.outbox.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEntityCommandRepository extends JpaRepository<OutboxEntity, Long> {
}
