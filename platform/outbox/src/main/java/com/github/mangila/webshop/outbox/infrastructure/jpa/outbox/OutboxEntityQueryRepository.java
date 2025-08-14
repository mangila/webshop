package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {

    @Query("""
            SELECT o.id FROM OutboxEntity o
            WHERE o.status = :status
            ORDER BY o.created ASC
            LIMIT :limit
            """)
    List<Long> findAllIdsByStatusAndDateBefore(@Param("status") OutboxStatusType status,
                                               @Param("limit") int limit);
}