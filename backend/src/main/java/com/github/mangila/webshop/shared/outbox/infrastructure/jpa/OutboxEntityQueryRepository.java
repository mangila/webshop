package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {
//    @Query("""
//              SELECT e FROM OutboxEvent e
//                WHERE e.topic = :#{#query.topic()}
//                AND e.aggregateId = :#{#query.aggregateId()}
//                AND e.value >= :#{#query.offset()}
//                ORDER BY e.value
//                LIMIT :#{#query.limit()}
//            """)
//    List<OutboxEvent> replay(@Param("query") EventReplayQuery query);

}
