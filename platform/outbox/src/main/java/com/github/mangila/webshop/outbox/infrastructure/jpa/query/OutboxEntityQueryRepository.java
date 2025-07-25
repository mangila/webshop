package com.github.mangila.webshop.outbox.infrastructure.jpa.query;

import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntity;
import com.github.mangila.webshop.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEntityQueryRepository extends JpaRepository<OutboxEntity, Long> {

    List<OutboxMessageProjection> findAllByPublished(boolean published, Sort sort, Limit limit);
}
