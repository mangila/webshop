package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OutboxSequenceEntityQueryRepository extends JpaRepository<OutboxSequenceEntity, UUID> {
}
