package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {
//    @Query("""
//              SELECT e FROM OutboxEvent e
//                WHERE e.value = :#{#query.value()}
//                AND e.aggregateId = :#{#query.aggregateId()}
//                AND e.value >= :#{#query.offset()}
//                ORDER BY e.value
//                LIMIT :#{#query.limit()}
//            """)
//    List<OutboxEvent> replay(@Param("query") EventReplayQuery query);


    @Query(value = """
            SELECT id,aggregate_id,domain,event FROM outbox WHERE published = :published
            FOR UPDATE SKIP LOCKED LIMIT :limit
            """, nativeQuery = true)
    List<OutboxMessageProjection> findAllByPublished(@Param("published") boolean published, @Param("limit") int limit);
}
