package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {
}
