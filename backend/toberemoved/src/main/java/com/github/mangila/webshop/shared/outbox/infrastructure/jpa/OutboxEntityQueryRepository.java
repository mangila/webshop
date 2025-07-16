package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {

    @Transactional
    @Query(value = """
            SELECT id,aggregate_id,domain,event FROM outbox
            WHERE published = :published
            FOR UPDATE SKIP LOCKED LIMIT :limit
            """, nativeQuery = true)
    List<OutboxMessageProjection> findAllByPublished(@Param("published") boolean published, @Param("limit") int limit);
}
