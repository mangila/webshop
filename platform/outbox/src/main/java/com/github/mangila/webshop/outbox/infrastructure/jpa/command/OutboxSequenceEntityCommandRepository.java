package com.github.mangila.webshop.outbox.infrastructure.jpa.command;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxSequenceEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface OutboxSequenceEntityCommandRepository extends JpaRepository<OutboxSequenceEntity, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s FROM OutboxSequenceEntity s
            WHERE s.aggregateId = :aggregateId
            """)
    Optional<OutboxSequenceEntity> findAndLockByAggregateId(@Param("aggregateId") UUID aggregateId);
}
