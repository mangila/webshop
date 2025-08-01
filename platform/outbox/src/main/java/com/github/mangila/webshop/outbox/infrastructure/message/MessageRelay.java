package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.message.task.OutboxTaskRunner;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.message-relay.enabled", havingValue = "true")
public class MessageRelay {
    private final OutboxTaskRunner outboxTaskRunner;

    public MessageRelay(OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @PostConstruct
    public void init() {
        fillProductQueue();
    }

    @Scheduled(fixedRateString = "${app.message-relay.fill-queue-task.fixed-rate}")
    public void fillProductQueue() {
        OutboxTaskKey key = outboxTaskRunner.findKeyOrThrow("PRODUCT_FILL_QUEUE");
        outboxTaskRunner.runTask(key);
    }

    @Scheduled(fixedRateString = "${app.message-relay.process-queue-task.fixed-rate}")
    public void processProductQueue() {
        OutboxTaskKey key = outboxTaskRunner.findKeyOrThrow("PRODUCT_PROCESS_QUEUE");
        outboxTaskRunner.runTask(key);
    }

    @Scheduled(fixedRateString = "${app.message-relay.process-dlq-task.fixed-rate}")
    public void processProductDlq() {
        OutboxTaskKey key = outboxTaskRunner.findKeyOrThrow("PRODUCT_PROCESS_DLQ");
        outboxTaskRunner.runTask(key);
    }
}
