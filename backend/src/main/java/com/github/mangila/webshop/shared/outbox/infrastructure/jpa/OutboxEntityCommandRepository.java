package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OutboxEntityCommandRepository extends JpaRepository<OutboxEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE OutboxEntity e SET e.published = true WHERE e.id = :id")
    void updateAsPublished(@Param("id") long id);
}
