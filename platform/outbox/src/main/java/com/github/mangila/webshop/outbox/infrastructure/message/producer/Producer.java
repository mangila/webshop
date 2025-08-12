package com.github.mangila.webshop.outbox.infrastructure.message.producer;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.model.EventSource;

import java.util.function.Function;

public sealed interface Producer permits SpringEventProducer {

    void produce(Outbox outbox);

    default Function<Outbox, Outbox> produce() {
        return outbox -> {
            produce(outbox);
            return outbox;
        };
    }

    EventSource source();
}
