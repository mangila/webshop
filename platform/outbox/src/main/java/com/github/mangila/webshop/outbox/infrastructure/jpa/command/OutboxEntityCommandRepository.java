package com.github.mangila.webshop.outbox.infrastructure.jpa.command;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
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
                      AND status != 'PUBLISHED'
                    FOR UPDATE SKIP LOCKED
                    """,
            nativeQuery = true
    )
    Optional<OutboxEntity> findByIdWhereStatusNotPublishedForUpdate(@Param("id") long id);



    @Modifying
    @Query(
            value = """
                    UPDATE outbox SET
                    status = :status,
                    updated = :updated
                    WHERE id = :id
                    """,
            nativeQuery = true
    )
    void updateStatus(@Param("id") long id,
                      @Param("status") String status,
                      @Param("updated") Instant updated);
}
