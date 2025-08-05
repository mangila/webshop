package com.github.mangila.webshop.outbox.infrastructure.message.producer;

import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.shared.model.EventSource;

sealed interface Producer permits SpringEventProducer {

    void produce(OutboxProjection projection);

    EventSource source();
}
