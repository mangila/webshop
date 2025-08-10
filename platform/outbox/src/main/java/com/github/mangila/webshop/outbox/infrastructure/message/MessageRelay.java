package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.infrastructure.scheduler.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.task.OutboxTaskRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.outbox.message-relay.enabled", havingValue = "true")
public class MessageRelay {
    private final OutboxTaskRunner outboxTaskRunner;

    public MessageRelay(OutboxTaskRunner outboxTaskRunner) {
        this.outboxTaskRunner = outboxTaskRunner;
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-queue.fixed-rate}")
    public void productProcessQueue() {
        outboxTaskRunner.execute(new OutboxTaskKey("PRODUCT_PROCESS_QUEUE"));
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-dlq.fixed-rate}")
    public void productProcessDlq() {
        outboxTaskRunner.execute(new OutboxTaskKey("PRODUCT_PROCESS_DLQ"));
    }
}
