package com.github.mangila.webshop.outboxevent.infrastructure.repository;

import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxEventQueryJpaRepository extends JpaRepository<OutboxEvent, Long> {

//    @Query("""
//              SELECT e FROM OutboxEvent e
//                WHERE e.topic = :#{#query.topic()}
//                AND e.aggregateId = :#{#query.aggregateId()}
//                AND e.id >= :#{#query.offset()}
//                ORDER BY e.id
//                LIMIT :#{#query.limit()}
//            """)
//    List<OutboxEvent> replay(@Param("query") EventReplayQuery query);
}
