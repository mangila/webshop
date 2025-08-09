package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.infrastructure.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.task.OutboxSimpleTaskRunner;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.outbox.message-relay.enabled", havingValue = "true")
public class MessageRelay {
    private final OutboxSimpleTaskRunner outboxSimpleTaskRunner;

    public MessageRelay(OutboxSimpleTaskRunner outboxSimpleTaskRunner) {
        this.outboxSimpleTaskRunner = outboxSimpleTaskRunner;
    }

    @PostConstruct
    public void init() {
        productFillQueue();
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.fill-queue-task.fixed-rate}")
    public void productFillQueue() {
        OutboxTaskKey key = outboxSimpleTaskRunner.findKey("PRODUCT_FILL_QUEUE");
        outboxSimpleTaskRunner.execute(key);
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-queue-task.fixed-rate}")
    public void productProcessQueue() {
        OutboxTaskKey key = outboxSimpleTaskRunner.findKey("PRODUCT_PROCESS_QUEUE");
        outboxSimpleTaskRunner.execute(key);
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-dlq-task.fixed-rate}")
    public void productProcessDlq() {
        OutboxTaskKey key = outboxSimpleTaskRunner.findKey("PRODUCT_PROCESS_DLQ");
        outboxSimpleTaskRunner.execute(key);
    }
}
