package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
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

    @Query("""
                    SELECT o.id FROM OutboxEntity o
                    WHERE o.published = :published
                    ORDER BY o.created ASC
                    LIMIT :limit
            """)
    List<Long> findAllIdsByPublished(@Param("published") boolean published, @Param("limit") int limit);
}
