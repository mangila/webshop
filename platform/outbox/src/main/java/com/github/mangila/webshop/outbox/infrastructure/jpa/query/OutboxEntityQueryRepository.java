package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxEntityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
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
    List<OutboxEntity> replay(@Param("aggregateId") UUID aggregateId,
                              @Param("sequence") int sequence,
                              @Param("limit") int limit);

    @Query(value = """
                    SELECT id,
                           aggregate_id,
                           domain,
                           event,
                           payload,
                           sequence
                    FROM outbox
                    WHERE domain = :domain AND status = :status
                    ORDER BY created ASC
                    LIMIT :limit
            """,
            nativeQuery = true)
    List<OutboxEntityProjection> findAllProjectionByDomainAndStatus(
            @Param("domain") String domain,
            @Param("status") String status,
            @Param("limit") int limit);

    @Query("""
            SELECT o.id FROM OutboxEntity o
            WHERE o.status = :status
            AND o.created < :date
            ORDER BY o.created ASC
            LIMIT :limit
            """)
    List<Long> findAllIdsByStatusAndDateBefore(@Param("status") OutboxStatusType status,
                                               @Param("date") Instant date,
                                               @Param("limit") int limit);
}