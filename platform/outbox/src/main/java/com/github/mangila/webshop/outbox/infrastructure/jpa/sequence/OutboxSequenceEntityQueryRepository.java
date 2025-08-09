package com.github.mangila.webshop.outbox.infrastructure.jpa.sequence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxSequenceEntityQueryRepository extends JpaRepository<OutboxSequenceEntity, UUID> {
}
