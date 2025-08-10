package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
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
    List<OutboxEntity> replay(@Param("aggregateId") UUID aggregateId,
                              @Param("sequence") int sequence,
                              @Param("limit") int limit);

    @Query(value = """
                    SELECT o.id FROM OutboxEntity o
                    WHERE o.domain = :domain AND o.status = :status
                    ORDER BY o.created ASC
                    LIMIT :limit
            """)
    List<Long> findAllIdsByDomainAndStatus(
            @Param("domain") String domain,
            @Param("status") OutboxStatusType status,
            @Param("limit") int limit);

    @Query("""
            SELECT o.id FROM OutboxEntity o
            WHERE o.status = :status
            ORDER BY o.created ASC
            LIMIT :limit
            """)
    List<Long> findAllIdsByStatusAndDateBefore(@Param("status") OutboxStatusType status,
                                               @Param("limit") int limit);
}