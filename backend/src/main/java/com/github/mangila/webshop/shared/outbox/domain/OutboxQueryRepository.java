package com.github.mangila.webshop.shared.outbox.domain;


import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.outbox.infrastructure.jpa.projection.OutboxMessageProjection;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OutboxQueryRepository {

    Outbox findByIdOrThrow(OutboxId id) throws CqrsException;

    List<OutboxMessage> findAllByPublished(boolean published);
}
