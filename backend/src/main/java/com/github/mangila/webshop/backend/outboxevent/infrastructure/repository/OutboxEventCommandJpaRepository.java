package com.github.mangila.webshop.backend.outboxevent.infrastructure.repository;

import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventCommandJpaRepository extends JpaRepository<OutboxEvent, Long> {
}
