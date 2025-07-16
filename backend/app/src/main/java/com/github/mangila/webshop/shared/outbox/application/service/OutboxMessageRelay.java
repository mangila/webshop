package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import com.github.mangila.webshop.shared.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq.OutboxRabbitProducer;
import io.vavr.collection.Stream;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@ObservedService
public class OutboxMessageRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxMessageRelay.class);

    private final OutboxRepositoryGateway repository;
    private final OutboxRabbitProducer rabbitProducer;

    public OutboxMessageRelay(OutboxRepositoryGateway repository,
                              OutboxRabbitProducer rabbitProducer) {
        this.repository = repository;
        this.rabbitProducer = rabbitProducer;
    }

    @Transactional
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    void poll() {
        var outboxMessages = repository.query().findAllByPublished(false, 10);
        for (var message : outboxMessages) {
            Try.of(() -> publish(message))
                    .onSuccess(ok -> {
                        if (ok) {
                            repository.command().updateAsPublished(message.id());
                        } else {
                            log.error("Failed to publish message: {}", message);
                        }
                    })
                    .onFailure(e -> log.error("Failed to publish message: {}", message, e));
        }
    }

    private boolean publish(OutboxMessage message) {
        return Stream.of(message)
                .map(rabbitProducer::sendToStream)
                .map(CompletableFuture::join)
                .get();
    }
}
