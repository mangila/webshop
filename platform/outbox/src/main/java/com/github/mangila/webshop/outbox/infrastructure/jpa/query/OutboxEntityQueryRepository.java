package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {

    @Query("""
            SELECT o FROM OutboxEntity o
            WHERE o.aggregateId = :aggregateId
            AND o.sequence >= :sequence
            ORDER BY o.created ASC
            LIMIT :limit
            """)
    List<OutboxEntity> replay(@Param("aggregateId") UUID aggregateId, @Param("sequence") int sequence, @Param("limit") int limit);

    List<OutboxMessageProjection> findAllByPublished(boolean published, Sort sort, Limit limit);
}
