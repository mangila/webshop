package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;

import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface OutboxEntityCommandRepository extends JpaRepository<OutboxEntity, Long> {

    @Query(
            value = """
                    SELECT
                      id,
                      version,
                      domain,
                      event,
                      aggregate_id,
                      payload::jsonb,
                      sequence,
                      status,
                      updated,
                      created
                    FROM outbox
                    WHERE id = :id
                    FOR UPDATE SKIP LOCKED
                    """,
            nativeQuery = true
    )
    Optional<OutboxEntity> findByIdForUpdate(@Param("id") long id);

    @Modifying(
            clearAutomatically = true,
            flushAutomatically = true)
    @Query(
            value = """
                     UPDATE OutboxEntity o
                     SET o.status = :status,
                         o.updated = :updated,
                         o.version = o.version + 1
                     WHERE o.id = :id
                    """
    )
    void updateStatus(@Param("id") long id,
                      @Param("status") OutboxStatusType status,
                      @Param("updated") Instant updated);
}
