package com.github.mangila.webshop.backend.event.infrastructure;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventFindByTopicAndTypeAndOffsetQuery;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventQueryRepository extends JpaRepository<Event, Long> {

    @Query("""
              SELECT e FROM Event e
                WHERE e.topic = :#{#query.topic()}
                AND e.aggregateId = :#{#query.aggregateId()}
                AND e.id >= :#{#query.offset()}
                ORDER BY e.id
                LIMIT :#{#query.limit()}
            """)
    List<Event> replay(@Param("query") EventReplayQuery query);

    @Query("""
              SELECT e FROM Event e
                WHERE e.topic = :#{#query.topic()}
                AND e.type = :#{#query.type()}
                AND e.id > :#{#query.offset()}
                ORDER BY e.id
                LIMIT :#{#query.limit()}
            """)
    List<Event> consume(@Param("query") EventFindByTopicAndTypeAndOffsetQuery query);
}
