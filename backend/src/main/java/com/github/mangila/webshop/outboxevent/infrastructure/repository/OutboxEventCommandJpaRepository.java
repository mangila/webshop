package com.github.mangila.webshop.outboxevent.infrastructure.repository;

import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventCommandJpaRepository extends JpaRepository<OutboxEvent, Long> {

}
