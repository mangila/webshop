package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.jpa.repository.JpaRepository;

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


    List<OutboxMessageProjection> findAllByPublished(boolean published);

}
