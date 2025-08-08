package com.github.mangila.webshop.outbox.infrastructure.message.producer;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.model.EventSource;

sealed interface Producer permits SpringEventProducer {

    void produce(Outbox outbox);

    EventSource source();
}
