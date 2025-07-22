package com.github.mangila.webshop.outbox.infrastructure.jpa.command;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OutboxEntityCommandRepository extends JpaRepository<OutboxEntity, Long> {

    @Query(
            value = """
                    SELECT id,aggregate_id,domain,event
                    FROM outbox
                    WHERE id = :id AND published = false
                    ORDER BY created
                    FOR UPDATE SKIP LOCKED
                    """,
            nativeQuery = true
    )
    Optional<OutboxMessageProjection> findProjectionByIdAndPublishedFalseForUpdateOrThrow(@Param("id") long id);

    @Query(
            value = """
                    SELECT id,aggregate_id,domain,event
                    FROM outbox
                    WHERE published = :published
                    ORDER BY created
                    FOR UPDATE SKIP LOCKED LIMIT :limit
                    """,
            nativeQuery = true
    )
    List<OutboxMessageProjection> findAllProjectionsByPublishedForUpdate(@Param("published") boolean published, @Param("limit") int limit);

    @Modifying
    @Query(
            value = """
                    UPDATE outbox
                    SET published = :published
                    WHERE id = :id
                    """,
            nativeQuery = true
    )
    void updateAsPublished(@Param("id") long id, @Param("published") boolean published);
}
